import os
from flask import Flask, request
from modules.inference import predict_class

app = Flask(__name__)
pred = predict_class(home_path="./modules")

@app.route('/', methods=['GET', 'POST'])
def postHandler():
    imagefile = request.files['image']
    filename = imagefile.filename
    print("\nReceived image File name : " + imagefile.filename)
    imagefile.save(filename)
    return pred.predict(filename)

app.run(host='127.0.0.1', port=8001, debug=True)