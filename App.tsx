import React, {useEffect, useState} from 'react';
import {
  View,
  Text,
  FlatList,
  TouchableOpacity,
  NativeModules,
  StyleSheet,
  Linking,
  Dimensions,
  Alert,
  Image,
  SafeAreaView,
  ScrollView,
} from 'react-native';

interface AppInfo {
  name: string;
  packageName: string;
  icon: string; // Add icon property
  locked?: boolean; // Add locked property
}

const {AppListModule} = NativeModules;
const {width} = Dimensions.get('window');
const numColumns = 3;
const ROW_HEIGHT = 150; // Adjust this value based on your needs

const App = () => {
  const [apps, setApps] = useState<AppInfo[]>([]);
  const [focusedIndex, setFocusedIndex] = useState<number | string>(-1);

  useEffect(() => {
    loadApps();
  }, []);

  const loadApps = async () => {
    try {
      let installedApps = await AppListModule.getInstalledApps();

      console.log('[Launcher] Installed apps:', installedApps);

      // check if settings is there or not, if not add it
      const settingsApp = installedApps.find(
        (app: AppInfo) => app.packageName === 'com.android.settings',
      );

      // Specifically for Android TV
      if (!settingsApp) {
        installedApps.push({
          name: 'Settings',
          packageName: 'com.android.tv.settings',
          icon: '', // Add a placeholder or default icon
        });
      }

      setApps(installedApps);
      return installedApps;
    } catch (error) {
      console.error('[Launcher] Error in loadApps:', error);
      return [];
    }
  };

  const launchApp = async (packageName: string) => {
    try {
      if (packageName === 'com.android.tv.settings') {
        await Linking.sendIntent('android.settings.SETTINGS');
      } else {
        await AppListModule.launchApp(packageName);
      }
    } catch (error) {
      console.error('[Launcher] Error launching app:', error);
    }
  };

  const renderAppItem = ({item}: {item: AppInfo}) => (
    <TouchableOpacity
      key={item.packageName}
      style={[
        styles.appItem,
        focusedIndex === item.packageName && styles.appItemFocused,
      ]}
      onFocus={() => setFocusedIndex(item.packageName)}
      onBlur={() => setFocusedIndex(-1)}
      onPress={() => launchApp(item.packageName)}>
      <View style={styles.appIconPlaceholder}>
        {item.icon ? (
          <Image
            source={{uri: `data:image/png;base64,${item.icon}`}}
            style={styles.appIcon}
          />
        ) : (
          <Text style={styles.appIconText}>{item.name[0]}</Text>
        )}
      </View>
      <Text
        style={[
          styles.appName,
          focusedIndex === item.packageName && styles.appNameFocused,
        ]}>
        {item.name}
      </Text>
    </TouchableOpacity>
  );

  return (
    <SafeAreaView style={styles.container}>
      <FlatList
        data={apps}
        renderItem={renderAppItem}
        keyExtractor={item => item.packageName}
        numColumns={numColumns}
        showsVerticalScrollIndicator={false}
        contentContainerStyle={styles.gridContainer}
        ListHeaderComponent={
          <View style={styles.header}>
            <Text style={styles.headerText}>My Apps</Text>
          </View>
        }
      />
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
    backgroundColor: '#141414',
  },
  header: {
    paddingHorizontal: 10,
    paddingTop: 0,
    marginBottom: 5,
  },
  headerText: {
    color: '#fff',
    fontSize: 34,
    fontWeight: 'bold',
  },
  gridContainer: {
    paddingVertical: 16,
  },
  appItem: {
    flex: 1,
    margin: 8,
    height: ROW_HEIGHT,
    padding: 16,
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: 12,
    backgroundColor: '#1f1f1f',
    transform: [{scale: 1}],
  },
  appItemFocused: {
    backgroundColor: '#2c2c2c',
    transform: [{scale: 1}],
    borderColor: '#007AFF',
    borderWidth: 2,
    opacity: 0.5, // Reduce opacity for the entire item
  },
  appIconPlaceholder: {
    width: 70,
    height: 70,
    borderRadius: 100,
    borderColor: '#007AFF',
    borderWidth: 2,
    alignItems: 'center',
    justifyContent: 'center',
    marginBottom: 12,
  },
  appIcon: {
    width: 35,
    height: 35,
    borderRadius: 3,
  },
  appIconText: {
    color: '#fff',
    fontSize: 24,
    fontWeight: 'bold',
  },
  appName: {
    color: '#fff',
    fontSize: 16,
    textAlign: 'center',
    opacity: 0.9,
  },
  appNameFocused: {
    color: '#007AFF',
    opacity: 1, // Ensure the app name remains fully opaque
  },
});

export default App;
