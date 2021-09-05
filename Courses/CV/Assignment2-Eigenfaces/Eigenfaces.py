import numpy as np
import cv2
import glob, os, sys

# make output directory and output file
STUDENT_CODE = '2016147538'
FILE_NAME = 'output.txt'
if not os.path.exists(STUDENT_CODE):
    os.mkdir(STUDENT_CODE) # make directory
f = open(os.path.join(STUDENT_CODE, FILE_NAME), 'w')

# get percentage and file lists
percent = float(sys.argv[1]) # get input percentage

trains = glob.glob('faces_training/face*.pgm') # train: 39 file list
tests = glob.glob('faces_test/test*.pgm') # test: 5 file list
trains.sort() # sort in ascending order
tests.sort() # sort in ascending order

# 1. Select the number of principal components you use for this data.
# 2. Reconstruct all images in the training set using the number of PCs.
# 3. Recognize images in the test dataset using a simple nearest neighbor algorithm.

####################################################################################
#                   STEP 1. Principal Components Selection                         #
####################################################################################

# Make train data matrix
scale = cv2.imread(trains[0], cv2.IMREAD_GRAYSCALE).shape
mat = cv2.imread(trains[0], cv2.IMREAD_GRAYSCALE).astype(np.float64).flatten() # flatten
for k in range(1, len(trains)):
  img = cv2.imread(trains[k], cv2.IMREAD_GRAYSCALE).astype(np.float64).flatten() # flatten
  mat = np.vstack((mat, img)) # concatenate
mat = np.transpose(mat)  # convert row-wise data to column-wise data

# zerom mean
diff = np.mean(mat, axis=1)
for k in range(len(mat[0,:])):
  mat[:,k] -= diff

# Compute SVD
U, s, V = np.linalg.svd(mat, full_matrices=False)

# find dimension
SUM = np.sum(np.square(s)) # total sum
d = 0 # dimension
temp = 0 # temporal sum
for idx, component in enumerate(s):
  temp += np.square(component)
  if (temp/SUM >= percent):
    d = idx + 1
    break

# write output file
f.write('##########  STEP 1  ##########\n')
f.write('Input Percentage: ' + str(percent) + '\n')
f.write('Selected Dimension: ' + str(d) + '\n\n')

####################################################################################
#                   STEP 2. Image Reconstruction                                   #
####################################################################################
# projection
Y = np.dot(np.transpose(U[:, :d]), mat)

# reconstruction
Z = np.dot(U[:, :d], Y)
for k in range(len(Z[0,:])):
  Z[:, k] += diff # add mean
  img = (np.transpose(Z)[k]).reshape(scale) # reshaped image
  name = os.path.join(STUDENT_CODE, trains[k].split('/')[-1]) # location
  cv2.imwrite(name, img) # write reconstructed image

recons = glob.glob(os.path.join(STUDENT_CODE, 'face*.pgm')) # recons: 39 file list
recons.sort() # sort in ascending order

# calculate loss
losses = [] # save loss of each image
for k in range(len(trains)):
  org = cv2.imread(trains[k], cv2.IMREAD_GRAYSCALE).astype(np.float64).flatten()
  rcn = cv2.imread(recons[k], cv2.IMREAD_GRAYSCALE).astype(np.float64).flatten()
  loss = round((np.sum(np.square(np.subtract(org, rcn))) / len(org)), 4)
  losses.append(loss)
avg = round((np.sum(losses) / len(losses)), 4) # average loss

# write output file
f.write('##########  STEP 2  ##########\n')
f.write('Reconstruction error\n')
f.write('average : %.4f\n' %avg)
for k in range(len(trains)):
  f.write(trains[k].split('/')[-1].split('.')[0][4:] + ': %.4f\n' %losses[k])
f.write('\n')

####################################################################################
#                   STEP 3. Face Recognition                                       #
####################################################################################

# calculate L2 distance between constructed train and test
dist = [] # save minimum L2 distance
indices = [] # save minimum L2 distance train image index
for k in range(len(tests)):
  tst = cv2.imread(tests[k], cv2.IMREAD_GRAYSCALE).astype(np.float64).flatten() # test image
  tst = np.dot(np.transpose(U[:, :d]), tst) # projected test
  tst = np.dot(U[:, :d], tst) # reconstructed test
  for l in range(len(recons)):
    rcn = cv2.imread(recons[l], cv2.IMREAD_GRAYSCALE).astype(np.float64).flatten() # reconstructed train
    distance = np.sqrt(np.sum(np.square(np.subtract(rcn, tst)))) # calculate L2 distance
    if len(dist) < k+1: # first item
      dist.append(distance)
      indices.append(l)
    else: # compare to existing one
      if dist[k] > distance:
        dist[k] = distance
        indices[k] = l

# write output file
f.write('##########  STEP 3  ##########\n')
for k in range(len(tests)):
  f.write(tests[k].split('/')[-1] + ' ==> ' + trains[indices[k]].split('/')[-1] + '\n')

f.close() # close output.txt
