/**
 *
 *	MIT License
 *
 *	Copyright (c) 2023 Gautam Hazarika
 *
 *	Permission is hereby granted, free of charge, to any person obtaining a copy
 *	of this software and associated documentation files (the "Software"), to deal
 *	in the Software without restriction, including without limitation the rights
 *	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *	copies of the Software, and to permit persons to whom the Software is
 *	furnished to do so, subject to the following conditions:
 *
 *	The above copyright notice and this permission notice shall be included in all
 *	copies or substantial portions of the Software.
 *
 *	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *	SOFTWARE.
 *
 **/

package com.example.xpense.presentation.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.xpense.R

@Composable
fun About() {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.primary)
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic),
            contentDescription = "App Logo",
            modifier = Modifier.size(150.dp)
        )
        Text(text = "xPense", color = Color(0xFF5F7464), fontSize = 24.sp)
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "With love", fontStyle = FontStyle.Italic)
        Text(text = "from", fontStyle = FontStyle.Italic)
        Text(text = "Gautam Hazarika", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(24.dp))
        Row {
            Image(
                painter = painterResource(id = R.drawable.ic_twitter),
                contentDescription = "Twitter",
                modifier = Modifier
                    .size(40.dp)
                    .clickable {

                    }
            )
            Spacer(modifier = Modifier.width(16.dp))

            Image(
                painter = painterResource(id = R.drawable.ic_ig),
                contentDescription = "Instagram",
                modifier = Modifier
                    .size(40.dp)
                    .clickable {

                    }
            )
            Spacer(modifier = Modifier.width(16.dp))

            Image(
                painter = painterResource(id = R.drawable.ic_fb),
                contentDescription = "Facebook",
                modifier = Modifier
                    .size(40.dp)
                    .clickable {

                    }
            )
        }


    }
}