package com.example.lukas.tesonettest.custom

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.EditText
import com.example.lukas.tesonettest.R

/**
 * Created by lukas on 17.8.22.
 */
class NotEmptyEditText : EditText {
	constructor(context: Context) : super(context) {
		init(context, null)
	}

	constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
		init(context, attrs)

	}

	constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
		init(context, attrs)

	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
		init(context, attrs)
	}

	private fun init(context: Context, attrs: AttributeSet?) {
		setOnFocusChangeListener { v, hasFocus ->
			if (!hasFocus && text.toString().isBlank()) {
				error = context.getString(R.string.general_field_empty)
			}
		}
	}

}