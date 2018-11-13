# Script to transfer data from the Google Cloud bucket to the Google Cloud Virtual Machine
import os
input_text = "gsutil ls gs://plant0disease0image/data/Apple___Cedar_apple_rust/"
new_cmd = []
temp = ""
sub1 = input_text.replace("ls","cp")[:10]
for ele in os.popen(input_text):
	idx = ele.index("data")
	sub2 = ele[idx:len(ele)-1]
	os.system(sub1+ele[:len(ele)-1]+" "+sub2)