package com.sagrd.ejemplomapacompose.componentes

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
//
//@Composable
//fun SagTextField(
//    modifier: Modifier = Modifier,
//    value: String,
//    label: String,
//    onDoneActionClick: () -> Unit = {},
//    onClearClick: () -> Unit = {},
//    onFocusChanged: (FocusState) -> Unit = {},
//    onValueChanged: (String) -> Unit
//) {
//    OutlinedTextField(
//        modifier = modifier
//            .padding(16.dp,8.dp,16.dp,0.dp)
//            .fillMaxWidth()
//            .onFocusChanged { onFocusChanged(it) },
//        label = { Text(text = label) },
//        value = value,
//        onValueChange = { query ->
//            onValueChanged(query)
//        },
//        textStyle = MaterialTheme.typography.subtitle1,
//        singleLine = true,
//        trailingIcon = {
//            IconButton(
//                onClick = {
//                    onClearClick()
//                }
//            ) {
//                Icon(imageVector = Icons.Filled.Clear, contentDescription = "Clear")
//            }
//        },
//        keyboardActions = KeyboardActions(onDone = { onDoneActionClick() }),
//        keyboardOptions = KeyboardOptions(
//            imeAction = ImeAction.Done,
//            keyboardType = KeyboardType.Text
//        )
//    )
//}