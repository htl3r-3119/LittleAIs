import numpy as np
from keras.models import Sequential
from keras.layers import Conv2D, MaxPooling2D, Flatten, Dense
from pathlib import Path

test_images_path = Path("../res/t10k-images.idx3-ubyte")
test_labels_path = Path("../res/t10k-labels.idx1-ubyte")
train_images_path = Path("../res/train-images.idx3-ubyte")
train_labels_path = Path("../res/train-labels.idx1-ubyte")

save_path = Path("../out/params")


def read_data(labels_path, images_path):
    with open(labels_path, "rb") as labels_file:
        labels_bytes = bytearray(labels_file.read())
        magic_num_label = int(labels_bytes[0])
        for i in range(1, 4):
            magic_num_label <<= 8
            magic_num_label |= int(labels_bytes[i])
        label_count = int(labels_bytes[4])
        for i in range(4,8):
            label_count <<= 8
            label_count |= int(labels_bytes[i])
        encoding = np.eye(10)
        labels = np.array([encoding[i] for i in labels_bytes[8:]])

        with open(images_path, "rb") as images_file:
            images_bytes = bytearray(images_file.read())

            magic_num_img = int(images_bytes[0])
            for i in range(1,4):
                magic_num_img <<= 8
                magic_num_img |= int(images_bytes[i])

            image_count = int(images_bytes[4])
            for i in range(4,8):
                image_count <<= 8
                image_count |= int(images_bytes[i])

            rows = int(images_bytes[8])
            for i in range(9,12):
                rows <<= 8
                rows |= int(images_bytes[i])

            cols = int(images_bytes[12])
            for i in range(13,16):
                cols <<= 8
                cols |= int(images_bytes[i])
            size = rows*cols

            images = [
                (np.array(images_bytes[i:i+size], dtype=float)/255.).reshape(28, 28, 1)
                for i in range(16, len(images_bytes), size)
            ]
            images = np.array(images)

            return labels, images


if __name__ == '__main__':
    labels_train, images_train = read_data(train_labels_path, train_images_path)
    labels_test, images_test = read_data(test_labels_path, test_images_path)

    print(labels_train.shape)
    print(images_train.shape)

    print(labels_test.shape)
    print(images_test.shape)

    model = Sequential([
        Conv2D(32, 3, activation='relu', input_shape=(28, 28, 1)),
        MaxPooling2D(2),
        Flatten(),
        Dense(10, activation='softmax')
    ])

    model.compile(optimizer="adam", loss='categorical_crossentropy', metrics=['accuracy'])
    # model.fit(images_train, labels_train, epochs=10, validation_data=(images_test, labels_test))

    print(model.get_weights())

