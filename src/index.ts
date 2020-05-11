// @flow
import { NativeModules, processColor } from 'react-native';

const { RNToastSnackbar } = NativeModules;

interface ToastOptions {
	message: string;
	duration?: number;
	gravity?: 'TOP' | 'BOTTOM' | 'LEFT' | 'RIGHT';
}
interface IToast {
	LENGTH_SHORT: number;
	LENGTH_LONG: number;
	show: (toastOptions: ToastOptions) => void;
}

interface SnackbarAction {
	text: string;
	textColor?: string | number;
	onPress?: () => void;
}

interface SnackbarOptions {
	message: string;
	duration?: number;
	textColor?: string | number;
	backgroundColor?: string | number;
	action?: SnackbarAction | undefined;
	rtl?: boolean;
}
interface ISnackbar {
	LENGTH_SHORT: number;
	LENGTH_LONG: number;
	LENGTH_INDEFINITE: number;
	show: (snackbarOptions: SnackbarOptions) => void;
}

// Toast
const Toast: IToast = {
	LENGTH_SHORT: RNToastSnackbar.TOAST_SHORT,
	LENGTH_LONG: RNToastSnackbar.TOAST_LONG,
	show(toastOptions: ToastOptions) {
		RNToastSnackbar.showToast(toastOptions);
	},
};

// Snackbar
const Snackbar: ISnackbar = {
	LENGTH_SHORT: RNToastSnackbar.SNACKBAR_SHORT,
	LENGTH_LONG: RNToastSnackbar.SNACKBAR_LONG,
	LENGTH_INDEFINITE: RNToastSnackbar.SNACKBAR_INDEFINITE,
	show(snackbarOptions: SnackbarOptions) {
		const textColor =
			snackbarOptions.textColor && processColor(snackbarOptions.textColor);
		const backgroundColor =
			snackbarOptions.backgroundColor &&
			processColor(snackbarOptions.backgroundColor);
		const action = snackbarOptions.action;
		const actionTextColor =
			action && action.textColor && processColor(action.textColor);
		const onPressCallback = (action && action.onPress) || (() => {});

		const nativeOptions: SnackbarOptions = {
			...snackbarOptions,
			textColor,
			backgroundColor,
			action: snackbarOptions.action
				? {
						...snackbarOptions.action,
						text: snackbarOptions.action.text,
						textColor: actionTextColor,
				  }
				: undefined,
		};

		RNToastSnackbar.showSnackbar(nativeOptions, onPressCallback);
	},
};

export { Toast, Snackbar };
