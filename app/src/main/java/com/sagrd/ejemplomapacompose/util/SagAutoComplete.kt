package com.sagrd.ejemplomapacompose.util

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sagrd.ejemplomapacompose.models.SampleData
import java.util.*
import kotlin.collections.ArrayList

@Stable
interface CuidadesAutoComplete<T> :AutoCompleteEntity{
    val value :T
}

@Stable
interface AutoCompleteEntity {
    fun filtro(query: String):Boolean
}
//une y carga la los items que se an filtrado
@Stable
interface AutoCompleteScope<T:AutoCompleteEntity>:AutoCompleteDesignScope {
    var isSearching : Boolean
    fun filter(query:String)
    fun onItemSelected(block:ItemSelected<T> = {})
}

//diseño del la caja del auto complete
@Stable
interface AutoCompleteDesignScope {
    var boxWidthPercentage:Float
    var shouldWrapContentHeight : Boolean
    var boxMaxHeight : Dp
    var boxBoarderStroke : BorderStroke
    var boxShape : Shape
}

@Composable
fun SagAutoComplete(
    sampleData: List<SampleData>,
    label:String
) {
    val name = ArrayList<String>()

    if (sampleData.isNotEmpty()) {
        sampleData.forEach { sampleData ->
            name.add(sampleData.name)
        }
    }

    val autoCompleteEntities = name.isAutoCompleteEntities(
        filter = { item, query ->
            item.lowercase(Locale.getDefault()).startsWith(query.lowercase(Locale.getDefault()))
        }
    )

    AutoCompleteBox(
        items = autoCompleteEntities,
        itemContent = { item ->
            AutoCompleteItem(item.value)
        }
    ){
        var value by remember { mutableStateOf("")}
        val view = LocalView.current

        onItemSelected { item ->
            value = item.value
            filter(value)
            isSearching = false
        }

        SagTextField(
            value = value,
            label = label,
            onValueChanged = { query ->
                value = query
                filter(value)
            },
            onDoneActionClick = {
                view.clearFocus()
            },
            onClearClick = {
                value = ""
                filter(value)
                view.clearFocus()
            },
            onFocusChanged = { focusState ->
                if (focusState.isFocused) {
                    isSearching = true
                }else
                    isSearching = false
            },
            modifier = Modifier.testTag("AutoCompleteSearchBar")
        )
    }
}
//compone los resultados del auto complete
@Composable
fun AutoCompleteItem(value: String){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        Text(
            text = value,
            fontSize = 15.sp,
            color = Color.Black
        )
    }
}

//
typealias palabraFiltada<T> = (T,String) -> Boolean

fun <T> List<T>.isAutoCompleteEntities(filter:palabraFiltada<T>):List<CuidadesAutoComplete<T>>{
    return map{
        object : CuidadesAutoComplete<T>{
            override val value: T
                get() = it

            override fun filtro(query: String): Boolean {
                return filter(value,query)
            }
        }
    }
}

private typealias ItemSelected<T> = (T) -> Unit

class AutoCompleState<T:AutoCompleteEntity>(private val startItems : List<T>):AutoCompleteScope<T>{

    private var onItemSelectedBlock : ItemSelected<T>? = null

    fun selectItem(item : T){
        onItemSelectedBlock?.invoke(item)
    }

    var filteredItems by mutableStateOf(startItems)
    override var isSearching by mutableStateOf(false)
    override var boxWidthPercentage by mutableStateOf(0.9f)
    override var shouldWrapContentHeight by mutableStateOf(false)
    override var boxMaxHeight: Dp by mutableStateOf(TextFieldDefaults.MinHeight * 3)
    override var boxBoarderStroke by mutableStateOf(BorderStroke(2.dp, Color.Black))
    override var boxShape: Shape by mutableStateOf(RoundedCornerShape(8.dp))

    override fun filter(query: String) {
        filteredItems = startItems.filter { entity ->
            entity.filtro(query = query)
        }
    }

    override fun onItemSelected(block: ItemSelected<T>) {
        onItemSelectedBlock = block
    }
}

@Composable
fun <T: AutoCompleteEntity> AutoCompleteBox(
    items: List<T>,
    itemContent: @Composable (T) -> Unit,
    content: @Composable AutoCompleteScope<T>.() -> Unit
) {
    val autoCompleteState = remember { AutoCompleState(startItems = items) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        autoCompleteState.content()
        AnimatedVisibility(
            visible = autoCompleteState.isSearching
        ) {
            LazyColumn(
                modifier = Modifier
                    .autoComplete(autoCompleteState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(autoCompleteState.filteredItems.size) { index ->
                    Box(
                        modifier = Modifier.clickable {
                            autoCompleteState.selectItem(autoCompleteState.filteredItems[index])
                        }
                    ) {
                        itemContent(autoCompleteState.filteredItems[index])
                    }
                }
            }
        }
    }
}

//Modifer para el auto complete para el lazycolum de los filtros
private fun Modifier.autoComplete(
    autoCompleteItemScope: AutoCompleteDesignScope
): Modifier = composed {
    val baseModifier = if (autoCompleteItemScope.shouldWrapContentHeight)
        wrapContentHeight()
    else
        heightIn(0.dp, autoCompleteItemScope.boxMaxHeight)

    baseModifier
        .testTag("AutoCompleteBox")
        .fillMaxWidth(autoCompleteItemScope.boxWidthPercentage)
        .border(
            border = autoCompleteItemScope.boxBoarderStroke,
            shape = autoCompleteItemScope.boxShape
        )
}

//EL COMPONENTE QUE SE PINTA PARA
@Composable
fun SagTextField(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    onDoneActionClick: () -> Unit = {},
    onClearClick: () -> Unit = {},
    onFocusChanged: (FocusState) -> Unit = {},
    onValueChanged: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier
            .padding(16.dp,8.dp,16.dp,0.dp)
            .fillMaxWidth()
            .onFocusChanged { onFocusChanged(it) },
        label = { Text(text = label) },
        value = value,
        onValueChange = { query ->
            onValueChanged(query)
        },
        textStyle = MaterialTheme.typography.subtitle1,
        singleLine = true,
        trailingIcon = {
            IconButton(
                onClick = {
                    onClearClick()
                }
            ) {
                Icon(imageVector = Icons.Filled.Clear, contentDescription = "Clear")
            }
        },
        keyboardActions = KeyboardActions(onDone = { onDoneActionClick() }),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text
        )
    )
}