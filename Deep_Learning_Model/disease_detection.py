'''
Topic : Code for learning PlantVillage dataset and making Plant disease detection application using AlexNet

Platform Used : Python, TensorFlow, Android, Google Cloud

Data : https://github.com/spMohanty/PlantVillage-Dataset
Reference Paper : https://arxiv.org/abs/1604.03169
Reference Code : https://github.com/tensorflow/models/blob/master/research/slim/nets/alexnet.py
               : https://github.com/tensorflow/models/blob/master/research/slim/nets/alexnet_test.py

'''

from __future__ import absolute_import
from __future__ import division
from __future__ import print_function

import os
import sys
import alexnet
import numpy as np
from PIL import Image
import tensorflow as tf
from random import shuffle

curr_path = "data/"

def read_data_files():
    dirs = []
    dir_idx = 0
    label_idx_pair = []
    for directory in os.listdir(curr_path):
        dir_idx+=1
        if os.path.isfile(directory):
            continue
        else:
            dirs.append(directory)
            file_idx = 0
            for files in os.listdir(curr_path + directory):
                file_idx+=1
                label_idx_pair.append([dir_idx,file_idx])
    return np.array(label_idx_pair), dirs

def FullyConvolutional_net(X, num_classes, dropout_keep_prob=0.25):
    with alexnet.slim.arg_scope(alexnet.alexnet_v2_arg_scope()):                        # Dimension of inputs : [batch_size, height, width, 3]
        logits, _ = alexnet.alexnet_v2(X, num_classes)                                  # Dimension of Logits : [batch_size, num_classes]
        return logits

def net_loss_calc(prediction, Y):
    return tf.reduce_mean(tf.nn.softmax_cross_entropy_with_logits_v2(logits = prediction, labels = Y, name='Net_Loss'))

def optimization(net_loss, learn_rate):
    return tf.train.AdamOptimizer(learning_rate = learn_rate).minimize(net_loss)

def separate_data(pair_arr):
    temp = np.random.permutation(pair_arr)
    no_examples = pair_arr.shape[0]
    no_train_ex = int(0.8*no_examples)
    
    pair_arr_train = temp[0:no_train_ex,:]
    pair_arr_test = temp[no_train_ex:no_examples,:]

    return pair_arr_train, pair_arr_test

def train_model(epochs, minibatch_size, sess, init, X, Y, nn_optimizer, net_loss, pair_arr_train, num_classes, dirs):
    tre = 0
    add_loss = 0
    interval = 1

    print('\n')

    for epoch in range(epochs):
        temp = np.random.permutation(pair_arr_train)
        for idx in range(0,len(pair_arr_train),minibatch_size):
            if (idx + minibatch_size) <= len(pair_arr_train):
                img_idxs = temp[idx:idx+minibatch_size]
                img_arr = np.zeros(0,float)
                img_labels = np.zeros((minibatch_size, num_classes))
                for img_idx,arr_idx in zip(img_idxs,range(len(img_idxs))):
                    img_labels[arr_idx][img_idx[0]-1] = 1
                    img = Image.open(curr_path + dirs[img_idx[0]-1] + '/' + str(img_idx[1]) + ".JPG")
                    img = img.resize((224,224))
                    img = np.asarray(img)
                    if len(img_arr) == 0:
                        img_arr = img.astype(float)
                    else:
                        img_arr = np.append(img_arr,img)

                img_arr = np.reshape(img_arr, [minibatch_size, 224, 224, 3])    # Resized Image

                [_, loss] = sess.run([nn_optimizer, net_loss], feed_dict = {X: img_arr, Y: img_labels})

                tre = tre + 1
                add_loss = add_loss + loss

        if epoch % interval == 0: 
            print("Epoch : " + str(epoch) + ", Current Loss : " + str(loss) + ", Average Loss : " + str(add_loss/tre))

def test_model(logits, X, sess, pair_arr_test, num_classes, dirs):

    test_records = len(pair_arr_test)
    img_arr = np.zeros(0,float)
    img_labels = np.zeros((test_records, num_classes))

    for idx in range(0,test_records):
        img_labels[idx][pair_arr_test[idx][0]-1] = 1
        img = Image.open(curr_path + dirs[pair_arr_test[idx][0]-1] + "/" + str(pair_arr_test[idx][1]) + ".JPG")
        img = img.resize((224,224))
        img = np.asarray(img)
        if len(img_arr) == 0:
            img_arr = img.astype(float)
        else:
            img_arr = np.append(img_arr,img)

    img_arr = np.reshape(img_arr, [test_records, 224, 224, 3])
    counter = 0
    # Getting Results for calculating accuracy
    result = tf.nn.softmax(logits, dim = 1)
    res = result.eval({X:img_arr}, session = sess)
    
    # Calculating Prediction Accuracy
    var = np.argmax(res, axis = 1)
    var1 = np.argmax(img_labels, axis = 1)
    
    counter = np.sum((np.equal(var, var1)).astype(int))
    accuracy = float(counter*100)/float(test_records)

    print('\nTrain Accuracy : ' + str(accuracy) + ' %')

def predict_func(logits, X, sess, img):
    # Predict the output of the image
    imgr = np.reshape(img, [1, 224, 224, 3])
    result = tf.nn.softmax(logits, dim = 1)
    res = result.eval({X:imgr}, session = sess)
    var = np.argmax(res)
    return var

def save_model(sess, path, saver):
    # Save the model to the disk.
    save_path = saver.save(sess, path)
    print("\nModel saved in file: " + str(save_path))

def restore_model(sess, path, saver):
    # restore the model from the disk.
    print(path)
    if os.path.isfile(path+'.index') == True:
        saver.restore(sess, path)
        print("\nModel is Restored")
    else:
        print("\nSorry, Model not found!!!")
        exit()

if __name__ == "__main__":  # Main function
   
    if len(sys.argv) > 2:
        print("\nArgument Error")
        exit()
    elif len(sys.argv) == 2:
        if sys.argv[1] == "1":    # Train and save the model
            flag = 1
        elif sys.argv[1] == "2":  # Restore and test the model
            flag = 2
        elif sys.argv[1] == "3":  # Restore and train the model further
            flag = 3
        else:
            print("\nArgument Error")
            exit()
    else:
        flag = 4                  # Predict the class of the data

    project_name = 'hackathon_Alex_Net'                 # Project Name
    path = 'model/' + project_name + '.ckpt'            # Path to save variables
    
    learn_rate = 0.03
    no_epochs = 1

    minibatch_size = 100
    height, width = 224, 224
    num_classes = 4
    pr = 0                      # Stores prediction result

    tf.reset_default_graph()    # Resetting Tensorflow graph
    sess = tf.Session()         # Making Tensorflow session

    X = tf.placeholder(tf.float32, shape=(None, 224, 224, 3))
    Y = tf.placeholder(tf.float32, shape=(None, num_classes))

    logits = FullyConvolutional_net(X, num_classes)
    net_loss = net_loss_calc(logits, Y)
    nn_optimizer = optimization(net_loss, learn_rate)

    init = tf.global_variables_initializer()
    sess.run(init)
    saver = tf.train.Saver()                    # Invoking the checkpoint saver

    pair_arr, dirs = read_data_files()
    pair_arr_train, pair_arr_test = separate_data(pair_arr)

    if flag == 1:
        train_model(no_epochs, minibatch_size, sess, init, X, Y, nn_optimizer, net_loss, pair_arr_train, num_classes, dirs)
        save_model(sess, path, saver)

    if flag == 2 or flag == 3 or flag == 4:
        restore_model(sess, path, saver)
        if flag == 2:
            test_model(logits, X, sess, pair_arr_test, num_classes, dirs)
        elif flag == 3:
            train_model(no_epochs, minibatch_size, sess, init, X, Y, nn_optimizer, net_loss, pair_arr_train, num_classes, dirs)
            save_model(sess, path, saver)
        elif flag == 4:
            img_data_path = 'data/Apple___Apple_scab/16.JPG'
            img = Image.open(img_data_path)
            img = img.resize((224,224))
            img = np.asarray(img)
            pr = predict_func(logits, X, sess, img)
            print("\nPrediction : " + str(pr))
