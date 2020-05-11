# react-native-toast-snackbar

## Getting started :point_left:

`$ npm install react-native-toast-snackbar --save`

### Mostly automatic installation

`$ react-native link react-native-toast-snackbar`

## Usage

### Only for ANDROID

```javascript
import { Toast, Snackbar } from 'react-native-toast-snackbar';

// Toast
Toast.show({
	message: 'My Message',
	duration: Toast.LENGTH_SHORT,
});

// Snackbar
Snackbar.show({
	message: 'My Message',
	duration: Snackbar.LENGTH_SHORT,
});
```

## Options

### `Toast.show(toastOptions)`

| Key      | Type   | Default            | description                   |
| -------- | ------ | ------------------ | ----------------------------- |
| message  | string | required           | The message to display        |
| duration | number | Toast.LENGTH_SHORT | How long to display message   |
| gravity  | string | undefined          | Position of the toast message |

Where `duration` is

1. `Toast.LENGTH_SHORT`
2. `Toast.LENGTH_LONG`

Where gravity is `'TOP' | 'BOTTOM' | 'LEFT' | 'RIGHT'`

### `Snackbar.show(snackbarOptions)`

| Key             | Type    | Default                | description                                                                                     |
| --------------- | ------- | ---------------------- | ----------------------------------------------------------------------------------------------- |
| message         | string  | required               | The message to display                                                                          |
| duration        | number  | Snackbar.LENGTH_SHORT  | How long to display message                                                                     |
| textColor       | string  | 'white'                | The color of message text                                                                       |
| backgroundColor | string  | undefined, 'dark gray' | The color of snackbar background                                                                |
| action          | object  | undefined              | The action button on snackbar                                                                   |
| rtl             | boolean | false                  | To display snackbar right-to-left direction (add `android:supportsRtl="true"` in manifest file) |

Where `duration` is

1. `Snackbar.LENGTH_SHORT`
2. `Snackbar.LENGTH_LONG`
3. `Snackbar.LENGTH_INDEFINITE`

Where `action` is

| Key       | Type     | Default   | description                                         |
| --------- | -------- | --------- | --------------------------------------------------- |
| text      | string   | required  | The text for snackbar button                        |
| textColor | string   | '#479ed7' | The color of text for button                        |
| onPress   | function | undefined | A callback function when user tap on snackbar buton |
