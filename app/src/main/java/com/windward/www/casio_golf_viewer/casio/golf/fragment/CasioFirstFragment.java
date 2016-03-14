/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.windward.www.casio_golf_viewer.casio.golf.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.windward.www.casio_golf_viewer.R;
import com.windward.www.casio_golf_viewer.casio.golf.util.ScreenUtil;

public class CasioFirstFragment extends Fragment {
	private int position;
	private static final String POSITION = "position";

	public static CasioFirstFragment getFragment(int position) {
		Bundle bundle = new Bundle();
		bundle.putInt(POSITION, position);
		CasioFirstFragment casioFirstFragment = new CasioFirstFragment();
		//setArguments()是个好东西,涉及到屏幕旋转
		casioFirstFragment.setArguments(bundle);
		return casioFirstFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		position = getArguments().getInt(POSITION);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		View contentView=LayoutInflater.from(getContext()).inflate(R.layout.view_no_image_1,null);
		ScreenUtil.initScale(contentView);
		contentView.setLayoutParams(layoutParams);
		initFirstFragmentViews(contentView);
		return contentView;

	}

	private void initFirstFragmentViews(View view){
		if(null!=view){

		}
	}


}