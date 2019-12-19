import os
import numpy as np 

import torch
import torch.nn.functional as F

import torchvision
import torchvision.models as models
import torch.utils.model_zoo as model_zoo
import torchvision.transforms as transforms

from collections import OrderedDict
import random

from PIL import Image

# Test accuracy: 91.60 %
# See Training and Testing Notebooks in 'archive' folder

class predict_class():
    def __init__(self, home_path = "./"):
        self.num_classes = 39    # Number of Classes
        self.net = None          # Stores Network model
        self.resize = (224, 224) # Image resize
        self.image_transform = transforms.Compose([transforms.Resize(256), transforms.CenterCrop(max(self.resize)), transforms.ToTensor(),
                                                transforms.Normalize([0.485, 0.456, 0.406], [0.229, 0.224, 0.225])])    # PIL Image Transform
        self.idx_to_name = {}   # Creating Label index to label name 
        self.home_path = home_path
    
        # Initializing variables
        self.init_idx_dict()
        self.load_model()

    def init_idx_dict(self):
        self.idx_to_name[28] = "Tomato Bacterial Spot"
        self.idx_to_name[29] = "Tomato Early Blight"
        self.idx_to_name[30] = "Tomato Late Blight"
        self.idx_to_name[31] = "Tomato Leaf Mold"
        self.idx_to_name[32] = "Tomato Septoria Leaf Spot"
        self.idx_to_name[33] = "Tomato Spider mites Two-spotted spider mite"
        self.idx_to_name[34] = "Tomato Target Spot"
        self.idx_to_name[35] = "Tomato yellow leaf curl virus"
        self.idx_to_name[36] = "Tomato Mosaic Virus"
        self.idx_to_name[37] = "Healthy"
    
    def load_model(self):   # Function to load pre-trained Squeeze Net model
        filename = os.path.join(self.home_path, "saved_models/plant_village/Plant_Village_saved_model_Squeeze_Net.pth.tar")      # Loading for testing
        self.net = models.__dict__['squeezenet1_1'](num_classes=self.num_classes)                   # Loading Squeeze Net Model

        # Loading Pre-Trained weights into the model
        checkpoint = torch.load(filename, map_location='cpu')

        # Look at issue: 'https://discuss.pytorch.org/t/1686/4' for following snippet
        new_state_dict = OrderedDict()  # Used because pretrained weights are saved using DataParallel
        for k, v in checkpoint['state_dict'].items():
            name = k[7:]            # remove `module.`
            new_state_dict[name] = v

        self.net.load_state_dict(new_state_dict)    
        self.net.eval()
    
    def predict(self, image_path):
        img = Image.open(image_path)
        img = self.image_transform(img)  # Transforming PIL image
        img = img.unsqueeze(0)           # NCHW Format
        output = self.net(img)           # Extracting highest index (or class) from output
        idx = F.softmax(output, dim=1).max(1)[1].item()    # Predicting Class

        # Confining class bound for Tomato diseases only (Limitation of our Android app)
        if idx < 28 or idx == 38:
            idx = random.randint(28, 37) 
        return self.idx_to_name[idx]

if __name__ == '__main__':
    pred = predict_class()
    img_path = "../archive/blight_tomato.JPG"
    print(pred.predict(img_path))