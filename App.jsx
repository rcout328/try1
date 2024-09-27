import React, { useEffect, useState } from 'react';
import { View, Text, PermissionsAndroid, DeviceEventEmitter, Button, NativeModules } from 'react-native';
import BackgroundService from 'react-native-background-actions';

const { SmsListenerModule } = NativeModules; // Import the native module

const App = () => {
  const [message, setMessage] = useState('');

  const requestSmsPermission = async () => {
    try {
      const permission = await PermissionsAndroid.request(PermissionsAndroid.PERMISSIONS.RECEIVE_SMS);
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
    if (SmsListenerModule) {
      console.log('SmsListenerModule is available');
      SmsListenerModule.startListeningToSMS();
      console.log('Called startListeningToSMS method');
    } else {
      console.log('SmsListenerModule is not available');
    }
  };

  const startBackgroundService = async () => {
    const task = async (taskData) => {
      // This will run in the background
      DeviceEventEmitter.addListener('onSMSReceived', (message) => {
        console.log('SMS received:', message);
        const { messageBody, senderPhoneNumber } = JSON.parse(message);
        setMessage(messageBody);
        console.log(`Message from ${senderPhoneNumber}: ${messageBody}`);
      });
    };

    const options = {
      taskName: 'SMS Listener',
      taskTitle: 'Listening for SMS',
      taskDesc: 'This app is running in the background to listen for SMS messages.',
      taskIcon: {
        name: 'ic_launcher', // Your app icon
        type: 'mipmap',
      },
      color: '#ff0000',
      linkingURI: 'yourapp://chat', // Optional
      parameters: {
        delay: 1000, // Optional
      },
    };

    await BackgroundService.start(task, options);
  };

  useEffect(() => {
    requestSmsPermission();
    startBackgroundService();

    return () => {
      DeviceEventEmitter.removeAllListeners('onSMSReceived'); // Clean up listener
      BackgroundService.stop();
    };
  }, []);

  return (
    <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
      <Text>SMS Data:</Text>
      <Text>{message}</Text>
      <Button title="Start Listening for SMS" onPress={checkSmsListener} />
    </View>
  );
};

export default App; 