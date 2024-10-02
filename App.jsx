import React, { useEffect, useState } from 'react';
import { View, Text, PermissionsAndroid, DeviceEventEmitter, Button, NativeModules } from 'react-native';
import BackgroundService from 'react-native-background-actions';

const { SmsListenerModule } = NativeModules; // Import the native module
const sleep = (time) => new Promise((resolve) => setTimeout(() => resolve(), time));

console.log('App.jsx is being executed');

const sendSmsToServer = async (messageBody) => {
  console.log('Attempting to send SMS to server:', messageBody);
  try {
    const response = await fetch('https://varun324242-s1.hf.space/predict', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        text: messageBody
      }),
    }); 
    const data = await response.json();
    console.log('Response from server:', data);
    return data;
  } catch (error) {
    console.error('Error sending SMS to server:', error);
  }
};

const backgroundTask = async (taskDataArguments) => {
  const { delay } = taskDataArguments;
  console.log('Background task started with delay:', delay);
  await new Promise( async (resolve) => {
    const smsListener = DeviceEventEmitter.addListener('onSMSReceived', async (message) => {
      console.log('SMS received in background:', message);
      const { messageBody } = JSON.parse(message);
      console.log('Parsed message body:', messageBody);
      const serverResponse = await sendSmsToServer(messageBody);
      console.log('Server response in background:', serverResponse);
    });

    console.log('Entering background task loop');
    while (BackgroundService.isRunning()) {
      console.log('Background service is still running');
      await sleep(delay);
    }

    smsListener.remove();
    console.log('Background task stopped');
    resolve();
  });
};

const App = () => {
  console.log('App component rendering');
  const [message, setMessage] = useState('');
  const [isBackgroundServiceRunning, setIsBackgroundServiceRunning] = useState(false);

  const requestSmsPermission = async () => {
    console.log('Requesting SMS permission');
    try {
      const permission = await PermissionsAndroid.request(PermissionsAndroid.PERMISSIONS.RECEIVE_SMS);
      console.log('SMS permission result:', permission);
      if (permission === PermissionsAndroid.RESULTS.GRANTED) {
        console.log('SMS permission granted');
      } else {
        console.log('SMS permission denied');
      }
    } catch (err) {
      console.log('Error requesting SMS permission:', err);
    }
  };

  const checkSmsListener = () => {
    console.log('Checking SMS Listener Module...');
    console.log('NativeModules:', JSON.stringify(NativeModules)); // Add this line for debugging
    if (SmsListenerModule) {
      console.log('SmsListenerModule is available');
      SmsListenerModule.startListeningToSMS();
      console.log('Called startListeningToSMS method');
    } else {
      console.log('SmsListenerModule is not available');
    }
  };

  const startBackgroundService = async () => {
    console.log('Starting background service');
    const options = {
      taskName: 'SMS Listener',
      taskTitle: 'Listening for SMS',
      taskDesc: 'This app is running in the background to listen for SMS messages.',
      taskIcon: {
        name: 'ic_launcher',
        type: 'mipmap',
      },
      color: '#ff0000',
      linkingURI: 'yourapp://chat',
      parameters: {
        delay: 5000,
      },
    };

    await BackgroundService.start(backgroundTask, options);
    setIsBackgroundServiceRunning(true);
    console.log('Background service started');
  };

  const stopBackgroundService = async () => {
    console.log('Stopping background service');
    await BackgroundService.stop();
    setIsBackgroundServiceRunning(false);
    console.log('Background service stopped');
  };

  useEffect(() => {
    console.log('App useEffect running');
    requestSmsPermission();

    const bootCompletedListener = DeviceEventEmitter.addListener('BootCompleted', () => {
      console.log('Boot completed event received');
      startBackgroundService();
    });

    const foregroundListener = DeviceEventEmitter.addListener('onSMSReceived', async (message) => {
      console.log('SMS received in foreground:', message);
      const { messageBody, senderPhoneNumber } = JSON.parse(message);
      setMessage(messageBody);
      console.log(`Message from ${senderPhoneNumber}: ${messageBody}`);
      
      const serverResponse = await sendSmsToServer(messageBody);
      console.log('Server response in foreground:', serverResponse);
    });

    console.log('Listeners set up');

    return () => {
      console.log('App useEffect cleanup');
      bootCompletedListener.remove();
      foregroundListener.remove();
      if (isBackgroundServiceRunning) {
        BackgroundService.stop();
      }
    };
  }, []);

  console.log('All Native Modules:', Object.keys(NativeModules));

  return (
    <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
      <Text>SMS Data:</Text>
      <Text>{message}</Text>
      <Button title="Start Listening for SMS" onPress={checkSmsListener} />
      <Button 
        title={isBackgroundServiceRunning ? "Stop Background Service" : "Start Background Service"} 
        onPress={isBackgroundServiceRunning ? stopBackgroundService : startBackgroundService}
      />
    </View>
  );
};

export default App;