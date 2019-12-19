# GreenDoc
Plant disease detection and remedy suggestion using Deep Learning, Server computing and Android.

## Team members
[Deep C.Patel](https://github.com/deepcpatel), [Satyak Patel](https://github.com/Satyak22), [Jay Vaghasiya](https://github.com/Jaysparkexel), [Hardik Shah](https://github.com/hrshah5) and [Priyansh Shah](https://github.com/pu37)

## Inspiration
The population on the globe is increasing rapidly and to support it we require enough food resources. But food production is already facing some serious problems such as Climate Change, Pollution and Plant Disease. According to Dr. David, the later one accounts for 42% loss of six major food crops on an average<sup>[1]</sup>. On the other hand, the adoption of smartphones with an active internet connection is rising. We can put these smartphones to good use by adding functionality to detect plant disease instantly and get a remedy for that, thus curing the disease if possible and avoid spreading it further. This inspired us to develop an application for plant disease detection by utilizing the existing technologies.

## What it does
It is meant to detect the plant disease from the snapshot of the plant leaf. All a user has to do is capture the plant leaf image from our app in his mobile. The app will then send this image to our AI system hosted on Cloud Platform or servers. Our AI will detect the disease form the image and send back the disease name, remedies, and precautions to the user.

## Platforms and Libraries
(1). **Server:** Flask server on Ubuntu 18.04.
<br />(2). **Android application:** Our application is successfully tested on Android 8.0
<br />(3). **Disease Recognition Model:** SqueezeNet model in PyTorch

## Disease detection model details
(1). We used SqueezeNet model<sup>[2]</sup> for disease classification because it is memory efficient due to less parameters and thus takes lesser training time.
<br />(2). We trained our classification model on [PlantVillage dataset](https://github.com/spMohanty/PlantVillage-Dataset)<sup>[3]</sup>. This dataset has leaf images of 14 different plants and 38 disease classes in total.
<br />(3). Instead of training our model from scratch, we used pre-trained weights for the training. This ensured excellent accuracy on the test data in less time.
<br />(4). Our training code is a modified version of original training script available [here](https://github.com/MarkoArsenovic/DeepLearning_PlantDiseases)<sup>[4]</sup>.
<br />(5). Test accuracy of our model is **91.60 %**.

## DevPost Link
Link to our similar hackation project: [DevPost Page](https://devpost.com/software/greendoc)

## References
[1]. D. Guest, "Special Issue Information", The Impact of Plant Disease on Food Security, 2012. [Link](https://www.mdpi.com/journal/agriculture/special_issues/plant_disease)
<br />[2]. F. N. Iandola, S. Han, M. W. Moskewicz, K. Ashraf, W. J. Dally, and K. Keutzer, "Squeezenet: Alexnet-level accuracy with 50x fewer parameters and < 0.5 mb model size", arXiv preprint arXiv:1602.07360, 2016.
<br />[3]. S. P. Mohanty, D. P. Hughes, and M. Salathé, “Using deep learning for image-based plant disease detection", Frontiers in plant science, vol. 7, p. 1419, 2016.
<br />[4]. B. Mohammed, A. Marko, L. Sohaib, S. Srdjan, B. Kamel, and M. Abdelouhab, "Deep Learning for Plant Diseases: Detection and Saliency Map Visualisation", Book: "Human and Machine Learning: Visible, 
<br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Explainable, Trustworthy and Transparent", Springer International Publishing, p. 93-117, 2018.