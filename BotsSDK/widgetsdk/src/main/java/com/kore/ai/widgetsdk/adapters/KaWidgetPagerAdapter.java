package com.kore.ai.widgetsdk.adapters;

import android.app.Activity;
import android.util.SparseArray;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * Created by Ramachandra Pradeep on 01-Mar-19.
 */

public class KaWidgetPagerAdapter extends FragmentPagerAdapter{
    private final SparseArray<Fragment> instantiatedFragments = new SparseArray<>();
    private Activity mContext;
//    private ArrayList<String> mTabHeader;
    private boolean shouldShowHome = true;

    public KaWidgetPagerAdapter(FragmentManager fm, Activity mContext, boolean shouldShowHome) {
        super(fm);
        this.mContext = mContext;
        this.shouldShowHome = shouldShowHome;
    }

    @Override
    public Fragment getItem(int position) {

        /*if(ACMEngine.PAGER_MODE == ACMEngine.MODE_LEFT_CHAT){
            switch (position) {
                case 0:
                    return KaWidgetFragment.newInstance();
                case 1:
                    KaChatContentFragment instance = KaChatContentFragment.newInstance();
                    instance.setComposeFooterInterface((KoraMainActivity) mContext);
                    instance.setInvokeGenericWebViewInterface((KoraMainActivity) (mContext));
                    return instance;
                *//*case 2:
                    return KaKnowledgeRightWidget.newInstance();*//*
                default:
                    return null;
            }
        }else*/ /*if(ACMEngine.PAGER_MODE == ACMEngine.MODE_ONLY_CHAT){*/
            switch (position) {
                /*case 0:
                    return KaWidgetFragment.newInstance();*/
//                case 0:
//                    KaChatContentFragment instance = KaChatContentFragment.newInstance();
////                    instance.setComposeFooterInterface((ComposeBarActivity) mContext);
////                    instance.setInvokeGenericWebViewInterface((ComposeBarActivity) (mContext));
//                    instance.setShouldShowHelp(shouldShowHome);
//                    return instance;
                /*case 2:
                    return KaKnowledgeRightWidget.newInstance();*/
                default:
                    return null;
            }
        /*}else if(ACMEngine.PAGER_MODE == ACMEngine.MODE_CHAT_RIGHT){
            switch (position) {
                *//*case 0:
                    return KaWidgetFragment.newInstance();*//*
                case 0:
                    KaChatContentFragment instance = KaChatContentFragment.newInstance();
                    instance.setComposeFooterInterface((KoraMainActivity) mContext);
                    instance.setInvokeGenericWebViewInterface((KoraMainActivity) (mContext));
                    return instance;
                case 1:
                    return KaKnowledgeRightWidget.newInstance();
                default:
                    return null;
            }
        }*//*else  {
            switch (position) {
                case 0:
                    return KaWidgetFragment.newInstance();
                case 1:
                    KaChatContentFragment instance = KaChatContentFragment.newInstance();
                    instance.setComposeFooterInterface((KoraMainActivity) mContext);
                    instance.setInvokeGenericWebViewInterface((KoraMainActivity) (mContext));
                    return instance;
                case 2:
                    return KaKnowledgeRightWidget.newInstance();
                default:
                    return null;
            }
        }*/
    }

    @Override
    public int getCount() {
        /*if(ACMEngine.PAGER_MODE == ACMEngine.MODE_LEFT_CHAT ||
                ACMEngine.PAGER_MODE == ACMEngine.MODE_CHAT_RIGHT) {
            return 2;
        }else*/ /*if(ACMEngine.PAGER_MODE == ACMEngine.MODE_ONLY_CHAT){
            return 1;
        }else {
            return 3;
        }*/
        return 1;
    }



    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final Fragment fragment = (Fragment) super.instantiateItem(container, position);
        instantiatedFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        instantiatedFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    @Nullable
    public Fragment getFragment(final int position) {
        final Fragment wr = instantiatedFragments.get(position);
        if (wr != null) {
            return wr;
        } else {
            return null;
        }
    }

    public Fragment getRegisteredFragment(int position) {
        return instantiatedFragments.get(position);
    }

}
