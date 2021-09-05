import cv2
import matplotlib.pyplot as plt
import numpy as np

##### To-do #####
def convert(img): # convert image to frequency domain
    new_img = np.zeros(img.shape)
    for i in range(img.shape[0]):  # fshift
        for j in range(img.shape[1]):
            new_img[i, j] = img[i, j] * (-1) ** (i + j)
    fshift = np.fft.fft2(new_img)
    return fshift

def fourier_transform(inputs):
    # 2d discrete fourier transform
    out = np.zeros(inputs.shape, dtype=np.complex128)
    for u in range(inputs.shape[0]):
        for v in range(inputs.shape[1]):
            for i in range(inputs.shape[0]):
                for j in range(inputs.shape[1]):
                    out[u,v] += inputs[i,j] * np.exp(-complex(0, 2*np.pi*(u*i/inputs.shape[0] + v*j/inputs.shape[1])))
    return out

def inverse_fourier_transform(inputs):
    # 2d discrete inverse fourier transform
    out = np.zeros(inputs.shape, dtype=np.complex128)
    for i in range(inputs.shape[0]):
        for j in range(inputs.shape[1]):
            for u in range(inputs.shape[0]):
                for v in range(inputs.shape[1]):
                    out[i,j] += inputs[u,v] * np.exp(complex(0, 2*np.pi*(u*i/inputs.shape[0] + v*j/inputs.shape[1])))
    out /=  (inputs.shape[0]*inputs.shape[1])
    return out

def fm_spectrum(img): # frequency magnitude spectrum image
    magnitude_spectrum = 20 * np.log(np.abs(convert(img))+1) # 1 for log 0 case
    return magnitude_spectrum

def low_pass_filter(img, th=20): # do low-pass filter
    f_img = convert(img)  # frequency domain image
    center = img.shape[0] // 2
    mask = np.zeros((img.shape[0], img.shape[1]))
    for i in range(img.shape[0]): # in side the circle then 1
        for j in range(img.shape[1]):
            if ((center - i) ** 2 + (center - j) ** 2) ** 0.5 <= th:
                mask[i][j] = 1
    masked_img = f_img * mask
    fishift = np.fft.ifft2(masked_img)
    new_img = np.zeros(img.shape)
    for i in range(img.shape[0]):  # ifftshift
        for j in range(img.shape[1]):
            new_img[i, j] = fishift[i, j] * (-1) ** (i + j)
    return new_img

def high_pass_filter(img, th=30): # do high-pass filter
    f_img = convert(img)  # frequency domain image
    center = img.shape[0] // 2
    mask = np.ones((img.shape[0], img.shape[1]))
    for i in range(img.shape[0]): # inside the circle then 0
        for j in range(img.shape[1]):
            if ((center - i) ** 2 + (center - j) ** 2) ** 0.5 <= th:
                mask[i][j] = 0
    masked_img = f_img * mask
    fishift = np.fft.ifft2(masked_img)
    new_img = np.zeros(img.shape)
    for i in range(img.shape[0]):  # ifftshift
        for j in range(img.shape[1]):
            new_img[i, j] = fishift[i, j] * (-1) ** (i + j)
    return new_img

def denoise1(img):
    f_img = convert(img)  # frequency domain image
    center = img.shape[0] // 2
    mask = np.ones((img.shape[0], img.shape[1]))  # gaussian mask
    sigma_s = 30 # standard deviation of gaussian
    for i in range(-center, center):  # calculate gaussian
        for j in range(-center, center):
            gs = np.exp(-(i ** 2 + j ** 2) / (2 * (sigma_s ** 2)))
            gs /= 2 * np.pi * (sigma_s ** 2)
            mask[i + center][j + center] = gs

    masked_img = f_img * mask
    fishift = np.fft.ifft2(masked_img)
    new_img = np.zeros(img.shape)
    for i in range(img.shape[0]):  # ifftshift
        for j in range(img.shape[1]):
            new_img[i, j] = fishift[i, j] * (-1) ** (i + j)
    return new_img

def denoise2(img):
    f_img = convert(img)
    center = img.shape[0] // 2
    mask = np.ones((img.shape[0], img.shape[1]))  # band-reject mask
    h_th = 42  # high_threshold
    l_th = 40  # low_threshold
    for i in range(img.shape[0]):
        for j in range(img.shape[1]):
            if (((center - i) ** 2 + (center - j) ** 2) ** 0.5 >= l_th) and (((center - i) ** 2 + (center - j) ** 2) ** 0.5 <= h_th):
                mask[i][j] = 0
    masked_img = f_img * mask
    fishift = np.fft.ifft2(masked_img)
    new_img = np.zeros(img.shape)
    for i in range(img.shape[0]):  # ifftshift
        for j in range(img.shape[1]):
            new_img[i, j] = fishift[i, j] * (-1) ** (i + j)
    return new_img

#################

if __name__ == '__main__':
    img = cv2.imread('task2_sample.png', cv2.IMREAD_GRAYSCALE)
    cor1 = cv2.imread('task2_corrupted_1.png', cv2.IMREAD_GRAYSCALE)
    cor2 = cv2.imread('task2_corrupted_2.png', cv2.IMREAD_GRAYSCALE)

    def drawFigure(loc, img, label):
        plt.subplot(*loc), plt.imshow(img, cmap='gray')
        plt.title(label), plt.xticks([]), plt.yticks([])

    drawFigure((2,7,1), img, 'Original')
    drawFigure((2,7,2), low_pass_filter(img), 'Low-pass')
    drawFigure((2,7,3), high_pass_filter(img), 'High-pass')
    drawFigure((2,7,4), cor1, 'Noised')
    drawFigure((2,7,5), denoise1(cor1), 'Denoised')
    drawFigure((2,7,6), cor2, 'Noised')
    drawFigure((2,7,7), denoise2(cor2), 'Denoised')

    drawFigure((2,7,8), fm_spectrum(img), 'Spectrum')
    drawFigure((2,7,9), fm_spectrum(low_pass_filter(img)), 'Spectrum')
    drawFigure((2,7,10), fm_spectrum(high_pass_filter(img)), 'Spectrum')
    drawFigure((2,7,11), fm_spectrum(cor1), 'Spectrum')
    drawFigure((2,7,12), fm_spectrum(denoise1(cor1)), 'Spectrum')
    drawFigure((2,7,13), fm_spectrum(cor2), 'Spectrum')
    drawFigure((2,7,14), fm_spectrum(denoise2(cor2)), 'Spectrum')

    plt.show()