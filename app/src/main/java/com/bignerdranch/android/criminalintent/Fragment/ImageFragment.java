package com.bignerdranch.android.criminalintent.Fragment;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bignerdranch.android.criminalintent.Utils.PictureUtils;

/**
 * Created by dell on 2016/3/19.
 */
public class ImageFragment extends DialogFragment {
    public static final String EXTRA_IMAGE_PATH =
            "com.bignerdranch.android.criminalintent.image_path";

    //将照片文件路径并放置到ImageFragment的argument bundle中
    public static ImageFragment newInstance(String imagePath){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_IMAGE_PATH, imagePath);
        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(args);
        fragment.setStyle(DialogFragment.STYLE_NO_TITLE,0);
        return fragment;
    }

    private ImageView mImageView;

    /**
     * 从argument获取文件路径，然后获取缩小版的图片，并设置给ImageView
     */
    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup parent, Bundle savedInstanceState){
        mImageView = new ImageView(getActivity());
        String path=(String) getArguments().getSerializable(EXTRA_IMAGE_PATH);
        BitmapDrawable image = PictureUtils.getScaledDrawable(getActivity(),path);
        mImageView.setImageDrawable(image);
        return mImageView;
    }

    /**
     * 在onDestroy卸载图片，释放内存
     */
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        PictureUtils.cleanImageView(mImageView);
    }

}
