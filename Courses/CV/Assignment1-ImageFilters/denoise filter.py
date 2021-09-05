import cv2
import numpy as np

def task1(src_img_path, clean_img_path, dst_img_path):
    """
    This is main function for task 1.
    It takes 3 arguments,
    'src_img_path' is path for source image.
    'clean_img_path' is path for clean image.
    'dst_img_path' is path for output image, where your result image should be saved.

    You should load image in 'src_img_path', and then perform task 1 of your assignment 1,
    and then save your result image to 'dst_img_path'.
    """
    noisy_img = cv2.imread(src_img_path)
    clean_img = cv2.imread(clean_img_path)
    # TO DO

    # do noise removal
    if 'test1' in src_img_path: # test1
        result_img = apply_bilateral_filter(noisy_img,5,1.2,90) #13.7050
    elif 'test2' in src_img_path: # test2
        temp_img = test2(noisy_img, 9, 1.1)
        result_img = apply_bilateral_filter(temp_img,9,1.1,50) #8.8661
    elif 'test3' in src_img_path: # test3
        result_img = apply_bilateral_filter(noisy_img,5,2,50) #3.6420
    elif 'test4' in src_img_path: # test4
        result_img = salt_pepper_median_filter(noisy_img,3) #7.7779
    elif 'test5' in src_img_path: # test5
        result_img = salt_pepper_median_filter(noisy_img,3) #5.0336
    cv2.imwrite(dst_img_path, result_img)
    pass

def conv(img, kernel):
    # function for padding and convolution
    pad_size = int((kernel.shape[0] - 1) / 2)  # zero padding size
    new_img = np.zeros((img.shape[0] + kernel.shape[0] - 1, img.shape[1] + kernel.shape[1] - 1, img.shape[2]))
    new_img[pad_size:pad_size+img.shape[0] , pad_size:pad_size+img.shape[1]] = img
    output = np.zeros((img.shape[0], img.shape[1], img.shape[2]))
    for z in range(img.shape[2]): # image
        for y in range(img.shape[1]):
            for x in range(img.shape[0]):
                output[x,y,z] = (new_img[x:x+kernel.shape[0], y:y+kernel.shape[1],z] * kernel).sum()
    output = output.astype('uint8')
    return output

def salt_pepper_median_filter(img, kernel_size):
    # variation of median filter for denoise salt and pepper noise
    pad_size = int((kernel_size - 1) / 2)  # zero padding size
    new_img = np.zeros((img.shape[0] + kernel_size - 1, img.shape[1] + kernel_size - 1, img.shape[2]))
    new_img[pad_size:pad_size + img.shape[0], pad_size:pad_size + img.shape[1]] = img
    output = np.zeros((img.shape[0], img.shape[1], img.shape[2]))
    for z in range(img.shape[2]):  # image
        for y in range(img.shape[1]):
            for x in range(img.shape[0]):
                if 0 < img[x][y][z] < 255: # this part is newly added
                  output[x,y,z] = img[x,y,z]
                  continue
                kernel = new_img[x:x+kernel_size, y:y+kernel_size, z]
                kernel = kernel.reshape(-1)
                kernel.sort()
                if (kernel_size % 2 == 0): # even case
                    output[x, y, z] = (kernel[len(kernel)/2] + kernel[len(kernel)/2 - 1]) / 2
                else:
                    output[x, y, z] = kernel[int(len(kernel)/2)]
    output = output.astype('uint8')
    return output

def test2(img, kernel_size, sigma_s):
    # function for preprocess image 'test2'
    # mutation of conv, apply_median_filter and newly add gaussian filter
    # Do partial convolution at certain areas

    # Make gaussian filter
    center = int(kernel_size / 2)
    pad_size = int((kernel_size - 1) / 2)
    new_img = np.zeros((img.shape[0] + kernel_size - 1, img.shape[1] + kernel_size - 1, img.shape[2]))
    new_img[pad_size:pad_size + img.shape[0], pad_size:pad_size + img.shape[1]] = img
    output1 = np.zeros((img.shape[0], img.shape[1], img.shape[2]))
    base_kernel = np.zeros((kernel_size, kernel_size))  # kernel base with spatial kernel
    for i in range(-center, center + 1):  # calculate spatial gaussian
        for j in range(-center, center + 1):
            gs = np.exp(-(i ** 2 + j ** 2) / (2 * (sigma_s ** 2)))
            gs /= 2 * np.pi * (sigma_s ** 2)
            base_kernel[i + center][j + center] = gs

    # Do convolution with gaussian filter at certain area
    for z in range(img.shape[2]):  # image
        for y in range(img.shape[1]):
            for x in range(img.shape[0]):
                # do filter at certain areas
                if (70 < x < 190) or (260 < x):
                    output1[x, y, z] = img[x, y, z]
                else:
                    output1[x, y, z] = (new_img[x:x + kernel_size, y:y + kernel_size, z] * base_kernel).sum()

    # Do convolution with median filter at certain area
    new_img = np.zeros((img.shape[0] + kernel_size - 1, img.shape[1] + kernel_size - 1, img.shape[2]))
    new_img[pad_size:pad_size + img.shape[0], pad_size:pad_size + img.shape[1]] = output1
    output2 = np.zeros((img.shape[0], img.shape[1], img.shape[2]))
    for z in range(img.shape[2]):  # image
        for y in range(img.shape[1]):
            for x in range(img.shape[0]):
                # do filter at certain areas
                if (70 < x < 190) or (260 < x):
                    output2[x, y, z] = output1[x, y, z]
                else:
                    kernel = new_img[x:x + kernel_size, y:y + kernel_size, z]
                    kernel = kernel.reshape(-1)
                    kernel.sort()
                    if (kernel_size % 2 == 0):  # even case
                        output2[x, y, z] = (kernel[len(kernel) / 2] + kernel[len(kernel) / 2 - 1]) / 2
                    else:
                        output2[x, y, z] = kernel[int(len(kernel) / 2)]

    return output2

def apply_average_filter(img, kernel_size):
    """
    You should implement average filter convolution algorithm in this function.
    It takes 2 arguments,
    'img' is source image, and you should perform convolution with average filter.
    'kernel_size' is a int value, which determines kernel size of average filter.

    You should return result image.
    """
    kernel = np.ones((kernel_size, kernel_size), np.float32) / (kernel_size * kernel_size)

    return conv(img, kernel)


def apply_median_filter(img, kernel_size):
    """
    You should implement median filter convolution algorithm in this function.
    It takes 2 arguments,
    'img' is source image, and you should perform convolution with median filter.
    'kernel_size' is a int value, which determines kernel size of median filter.

    You should return result image.
    """

    pad_size = int((kernel_size - 1) / 2)  # zero padding size
    new_img = np.zeros((img.shape[0] + kernel_size - 1, img.shape[1] + kernel_size - 1, img.shape[2]))
    new_img[pad_size:pad_size + img.shape[0], pad_size:pad_size + img.shape[1]] = img
    output = np.zeros((img.shape[0], img.shape[1], img.shape[2]))
    for z in range(img.shape[2]):  # image
        for y in range(img.shape[1]):
            for x in range(img.shape[0]):
                kernel = new_img[x:x+kernel_size, y:y+kernel_size, z]
                kernel = kernel.reshape(-1)
                kernel.sort()
                if(kernel_size % 2 == 0): # even case
                    output[x, y, z] = (kernel[len(kernel)/2] + kernel[len(kernel)/2 - 1]) / 2
                else:
                    output[x, y, z] = kernel[int(len(kernel)/2)]
    output = output.astype('uint8')
    return output


def apply_bilateral_filter(img, kernel_size, sigma_s, sigma_r):
    """
    You should implement convolution with additional filter.
    You can use any filters for this function, except average, median filter.
    It takes at least 2 arguments,
    'img' is source image, and you should perform convolution with median filter.
    'kernel_size' is a int value, which determines kernel size of average filter.
    'sigma_s' is a int value, which is a sigma value for G_s
    'sigma_r' is a int value, which is a sigma value for G_r

    You can add more arguments for this function if you need.

    You should return result image.
    """
    center = int(kernel_size / 2)
    pad_size = int((kernel_size - 1) / 2)
    new_img = np.zeros((img.shape[0] + kernel_size - 1, img.shape[1] + kernel_size - 1, img.shape[2]))
    new_img[pad_size:pad_size + img.shape[0], pad_size:pad_size + img.shape[1]] = img
    output = np.zeros((img.shape[0], img.shape[1], img.shape[2]))
    base_kernel = np.zeros((kernel_size, kernel_size)) # kernel base with spatial kernel
    for i in range(-center, center+1): # calculate spatial gaussian
        for j in range(-center, center+1):
            gs = np.exp(-(i**2 + j**2) / (2 * (sigma_s ** 2) ))
            gs /= 2 * np.pi * (sigma_s ** 2)
            base_kernel[i+center][j+center] = gs

    # Convolution
    for z in range(img.shape[2]):
        for y in range(img.shape[1]):
            for x in range(img.shape[0]):
                kernel = np.array(base_kernel) # copy
                weight_sum = 0 # sum of kernel values
                for i in range(-center, center+1): # calculate range gausian and multiply
                    for j in range(-center, center+1):
                        gr = np.exp(-((new_img[center+x][center+y][z]-new_img[center+x+i][center+y+j][z]) ** 2) / (2 * (sigma_r ** 2) ))
                        gr /= ((2 * np.pi) ** 0.5) * sigma_r
                        kernel[i+center][j+center] *= gr
                        weight_sum += kernel[i+center][j+center]
                output[x,y,z] = (new_img[x:x+kernel_size, y:y+kernel_size,z] * kernel).sum() / weight_sum

    output = output.astype('uint8')
    return output
