package org.protonaosp.systemui.dagger;

import static com.android.systemui.Dependency.ALLOW_NOTIFICATION_LONG_PRESS_NAME;
import static com.android.systemui.Dependency.LEAK_REPORT_EMAIL_NAME;

import android.content.Context;
import android.hardware.SensorPrivacyManager;
import android.os.Handler;
import android.os.PowerManager;

import androidx.annotation.Nullable;

import org.protonaosp.systemui.theme.ThemeOverlayControllerAicp;

import com.android.keyguard.KeyguardViewController;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.dock.DockManager;
import com.android.systemui.dock.DockManagerImpl;
import com.android.systemui.doze.DozeHost;
import com.android.systemui.media.dagger.MediaModule;
import com.android.systemui.plugins.qs.QSFactory;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.power.EnhancedEstimates;
import com.android.systemui.power.EnhancedEstimatesImpl;
import com.android.systemui.power.PowerUI;
import com.android.systemui.power.PowerNotificationWarnings;
import com.android.systemui.qs.dagger.QSModule;
import com.android.systemui.qs.tileimpl.QSFactoryImpl;
import com.android.systemui.recents.Recents;
import com.android.systemui.recents.RecentsImplementation;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.phone.DozeServiceHost;
import com.android.systemui.statusbar.phone.HeadsUpManagerPhone;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.KeyguardEnvironmentImpl;
import com.android.systemui.statusbar.phone.NotificationShadeWindowControllerImpl;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.phone.ShadeControllerImpl;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.BatteryControllerImpl;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.DeviceProvisionedControllerImpl;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.statusbar.policy.IndividualSensorPrivacyController;
import com.android.systemui.statusbar.policy.IndividualSensorPrivacyControllerImpl;
import com.android.systemui.statusbar.policy.SensorPrivacyController;
import com.android.systemui.statusbar.policy.SensorPrivacyControllerImpl;
import com.android.systemui.theme.ThemeOverlayController;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module(includes = {
        MediaModule.class,
        QSModule.class
})
public abstract class SystemUIModuleAicp {

    @SysUISingleton
    @Provides
    @Named(LEAK_REPORT_EMAIL_NAME)
    static String provideLeakReportEmail() {
        return "";
    }

    @Binds
    abstract EnhancedEstimates bindEnhancedEstimates(EnhancedEstimatesImpl enhancedEstimates);

    @Binds
    abstract NotificationLockscreenUserManager bindNotificationLockscreenUserManager(
            NotificationLockscreenUserManagerImpl notificationLockscreenUserManager);

    @Provides
    @SysUISingleton
    static BatteryController provideBatteryController(
            Context context,
            EnhancedEstimates enhancedEstimates,
            PowerManager powerManager,
            BroadcastDispatcher broadcastDispatcher,
            DemoModeController demoModeController,
            @Main Handler mainHandler,
            @Background Handler bgHandler) {
        BatteryController bC = new BatteryControllerImpl(
                context,
                enhancedEstimates,
                powerManager,
                broadcastDispatcher,
                demoModeController,
                mainHandler,
                bgHandler);
        bC.init();
        return bC;
    }

    @Provides
    @SysUISingleton
    static SensorPrivacyController provideSensorPrivacyController(
            SensorPrivacyManager sensorPrivacyManager) {
        SensorPrivacyController spC = new SensorPrivacyControllerImpl(sensorPrivacyManager);
        spC.init();
        return spC;
    }

    @Provides
    @SysUISingleton
    static IndividualSensorPrivacyController provideIndividualSensorPrivacyController(
            SensorPrivacyManager sensorPrivacyManager) {
        IndividualSensorPrivacyController spC = new IndividualSensorPrivacyControllerImpl(
                sensorPrivacyManager);
        spC.init();
        return spC;
    }

    @Binds
    @SysUISingleton
    public abstract QSFactory bindQSFactory(QSFactoryImpl qsFactoryImpl);

    @Binds
    abstract DockManager bindDockManager(DockManagerImpl dockManager);

    @Binds
    abstract NotificationEntryManager.KeyguardEnvironment bindKeyguardEnvironment(
            KeyguardEnvironmentImpl keyguardEnvironment);

    @Binds
    abstract ShadeController provideShadeController(ShadeControllerImpl shadeController);

    @SysUISingleton
    @Provides
    @Named(ALLOW_NOTIFICATION_LONG_PRESS_NAME)
    static boolean provideAllowNotificationLongPress() {
        return true;
    }

    @SysUISingleton
    @Provides
    static HeadsUpManagerPhone provideHeadsUpManagerPhone(
            Context context,
            StatusBarStateController statusBarStateController,
            KeyguardBypassController bypassController,
            GroupMembershipManager groupManager,
            ConfigurationController configurationController) {
        return new HeadsUpManagerPhone(context, statusBarStateController, bypassController,
                groupManager, configurationController);
    }

    @SysUISingleton
    @Provides
    static PowerUI.WarningsUI provideWarningsUi(PowerNotificationWarnings controllerImpl) {
        return controllerImpl;
    }

    @Binds
    abstract HeadsUpManager bindHeadsUpManagerPhone(HeadsUpManagerPhone headsUpManagerPhone);

    @Provides
    @SysUISingleton
    static Recents provideRecents(Context context, RecentsImplementation recentsImplementation,
            CommandQueue commandQueue) {
        return new Recents(context, recentsImplementation, commandQueue);
    }

    @Binds
    abstract DeviceProvisionedController bindDeviceProvisionedController(
            DeviceProvisionedControllerImpl deviceProvisionedController);

    @Binds
    abstract KeyguardViewController bindKeyguardViewController(
            StatusBarKeyguardViewManager statusBarKeyguardViewManager);

    @Binds
    abstract NotificationShadeWindowController bindNotificationShadeController(
            NotificationShadeWindowControllerImpl notificationShadeWindowController);

    @Binds
    abstract DozeHost provideDozeHost(DozeServiceHost dozeServiceHost);

    @Binds
    abstract ThemeOverlayController provideThemeOverlayController(ThemeOverlayControllerAicp themeOverlayController);

}
