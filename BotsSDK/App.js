/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

import React from 'react';
import ReactNative from 'react-native'
import {
  SafeAreaView,
  StyleSheet,
  ScrollView,
  View,
  Text,
  Button,
  StatusBar,
} from 'react-native';

import {
  Header,
  LearnMoreLinks,
  Colors,
  DebugInstructions,
  ReloadInstructions,
} from 'react-native/Libraries/NewAppScreen';
const { BotConnectionModule } = ReactNative.NativeModules;
const bot_id = 'st-b1ed5f83-a15a-54fa-8611-02610e497b4e';
const bot_name = 'Mashreq Bank Assist Dev';
const authorization = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJMb2FuMDAwMSIsInNjb3BlIjoiTU9MX1VTRVIiLCJTRVNTSU9OIjoiNjgwMGI0MmEtZjg1MS00OTAwLWFlYzQtNDA5MDM1N2UzYjQ1IiwiaGFzaCI6IjUwNzc2YjA0MGFhMWRlYjczZTJmZmNjMzhiNWU2M2Q1IiwidXNlcm5hbWUiOiJMb2FuMDAwMSIsImV4cCI6MTYxNzcwMzA2MX0.oU6LOHamHyaUu6U2FKGhFp6VHVT2hhORauOzAR1LG7ZI0wnCf04x2fEM_x5Z8xBQhogP2h8U9DwuGfQNORaccg";
const xauth = "6800b42a-f851-4900-aec4-4090357e3b45";
const identity = generateQuickGuid();

const App: () => React$Node = () => {

  BotConnectionModule.initialize(bot_id, bot_name, authorization, xauth, identity)

  return (
    <>
      <StatusBar barStyle="dark-content" />
      <View style={styles.ViewSection}>
      <Button
        style={styles.BotButton}
        title="Connect To Bot from React Native"
        onPress={() => BotConnectionModule.show()}
      />
      </View>
    </>
  );
};

function generateQuickGuid() {
  return Math.random().toString(36).substring(2, 15) +
      Math.random().toString(36).substring(2, 15);
}

const styles = StyleSheet.create({
  scrollView: {
    backgroundColor: Colors.lighter,
  },
   footer: {
    color: Colors.dark,
    fontSize: 12,
    fontWeight: '600',
    padding: 4,
    paddingRight: 12,
    textAlign: 'right',
  },
  ViewSection: {
    width: '100%',
    height: '100%',
    justifyContent: 'center',
    alignItems: 'center'
 },
 BotButton: {
  fontSize: 40,
  fontWeight: '400',
  width: '100%',
  height: '30%',
  backgroundColor: '#FF5E00',
  color: 'white'
}
});

export default App;
