import requests
import os
import cv2
from lxml import html
import shutil
import json
from keras.models import model_from_json
import numpy as np
import sys


main_url = 'https://portal.aut.ac.ir'
login_page = "https://portal.aut.ac.ir/aportal/"
login_captcha = 'https://portal.aut.ac.ir/aportal/PassImageServlet'
right_menu = 'https://portal.aut.ac.ir/aportal/regadm/style/menu/menu.student.jsp'

username = str()
password = str()

def filter_captcha(img):
    img = img[5:35, 5:145]
    n_img = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    color = ('b', 'g', 'r')
    selected_color_max, max_hist = 'n', cv2.calcHist([n_img], [0], None, [256], [0, 256]).max()
    min_hist = 10000
    selected_color_min = selected_color_max
    for i, col in enumerate(color):
        histr = cv2.calcHist([img], [i], None, [256], [0, 256])
        if histr.max() > max_hist:
            max_hist = histr.max()
            selected_color_max = col
        if histr.max() < min_hist:
            min_hist = histr.max()
            selected_color_min = col
    if max_hist < 1000:
        selected_color = selected_color_min
    else:
        selected_color = selected_color_max
    if selected_color == 'r':
        img[:, :, 1] = 0
        img[:, :, 2] = 0
    elif selected_color == 'g':
        img[:, :, 0] = 0
        img[:, :, 2] = 0
    elif selected_color == 'b':
        img[:, :, 1] = 0
        img[:, :, 0] = 0

    img = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    hist = cv2.calcHist([img], [0], None, [256], [0, 256])

    if hist.max() > 1100:
        img = cv2.threshold(img, 0, 255, cv2.THRESH_BINARY_INV | cv2.THRESH_OTSU)[1]
    else:
        img = cv2.threshold(img, 0, 255, cv2.THRESH_BINARY | cv2.THRESH_OTSU)[1]
    return img

def get_captcha(cookies):
    request = requests.get(login_captcha, headers={'Cookie': cookies}, stream=True)
    with open('bw_image', 'wb') as out_file:
        shutil.copyfileobj(request.raw, out_file)
    img = cv2.imread('bw_image')
    img = filter_captcha(img)
    return img

def login(model,session):
    cookies = 'JSESSIONID=' + str(
        session) + ';_ga=GA1.3.787589358.1533647200'
    captchas = []
    num = 20
    for i in range(0, num):
        captcha = ''
        img = get_captcha(cookies)
        # cv2.imshow('img', img)
        # cv2.waitKey(0)
        # cv2.destroyAllWindows()
        letters = []
        for j in range(0, 5):
            img1 = np.zeros((30, 27, 1))
            img1[:, :, 0] = img[:, j * 28 + 1: (j + 1) * 28]
            letters.append(img1)
        prediction = list(model.predict(np.array(letters)))
        for predict in prediction:
            # if float(1) in list(predict):
            captcha += (chr(list(predict).index(max(list(predict)))+97))
            # else:
            #     print(predict)
        captchas.append(captcha)
    letters = []
    for j in range(0, 5):
        letter = []
        for i in range(0, num):
            letter.append(captchas[i][j])
        letters.append(max(set(letter), key=letter.count))
    captcha = ''.join(letters)
    print(captcha)
    cFile = open("captcha.txt","w+")
    cFile.write(captcha)
    cFile.close()
    exit()
    # print('write the captcha:')
    # captcha = input()
    data = 'username=' + username + '&password=' + password + '&passline=' + captcha + '&login=%D9%88%D8%B1%D9%88%D8%AF+%D8%A8%D9%87+%D9%BE%D9%88%D8%B1%D8%AA%D8%A7%D9%84'
    request = requests.post(login_page + 'login.jsp?' + data, headers={'Cookie': cookies})
    # print(request.headers)
    # print()
    request = requests.post(right_menu, headers={'Cookie': cookies})
    if 'Set-Cookie' in request.headers.keys():
        return False, request.text, cookies
    else:
        print("DONE DONE DONE");return True, 'done', cookies

def main():
    # load json and create model
    json_file = open('model.json', 'r')
    loaded_model_json = json_file.read()
    json_file.close()
    model = model_from_json(loaded_model_json)
    # load weights into new model
    model.load_weights("model.h5")
    print("Loaded model from disk")
    cookies = login(model,sys.argv[1])[2]
    exit()

if __name__ == "__main__":
    main()
