package org.protonaosp.systemui;

import android.content.Context;

import org.protonaosp.systemui.dagger.DaggerGlobalRootComponentAicp;
import org.protonaosp.systemui.dagger.GlobalRootComponentAicp;

import com.android.systemui.SystemUIFactory;
import com.android.systemui.dagger.GlobalRootComponent;

public class SystemUIFactoryAicp extends SystemUIFactory {
    @Override
    protected GlobalRootComponent buildGlobalRootComponent(Context context) {
        return DaggerGlobalRootComponentAicp.builder()
                .context(context)
                .build();
    }
}
