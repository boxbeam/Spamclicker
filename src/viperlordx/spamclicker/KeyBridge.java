package viperlordx.spamclicker;

import java.awt.AWTException;
import java.awt.Robot;
import java.util.logging.LogManager;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class KeyBridge {
	static int output = 0;
	public static int getNativeKeyCode(int keycode) throws AWTException, NativeHookException {
		output = 0;
		NativeKeyListener listener = new NativeKeyListener() {
			@Override
			public void nativeKeyPressed(NativeKeyEvent arg0) {
			}
			@Override
			public void nativeKeyReleased(NativeKeyEvent arg0) {
				output = arg0.getKeyCode();
			}
			@Override
			public void nativeKeyTyped(NativeKeyEvent arg0) {
			}
		};
		GlobalScreen.registerNativeHook();
		LogManager.getLogManager().reset();
		GlobalScreen.addNativeKeyListener(listener);
		Robot robot = new Robot();
		robot.keyRelease(keycode);
		GlobalScreen.removeNativeKeyListener(listener);
		GlobalScreen.unregisterNativeHook();
		return output;
	}
}
