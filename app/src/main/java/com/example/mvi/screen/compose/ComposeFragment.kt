package com.example.mvi.screen.compose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.fragment.navArgs
import com.arch.mvi.view.StateEffectScaffold
import com.example.mvi.R
import com.example.mvi.ext.findActivity
import com.example.mvi.repo.Contact
import com.example.mvi.screen.compose.ComposeState.Companion.MAX_COUNT
import com.example.mvi.screen.compose.destinations.MVIScreenDestination
import com.example.mvi.test.TestTag
import com.example.mvi.ui.theme.MVIHiltTheme
import com.example.mvi.utils.ImageLoader.decodeBitmap
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ComposeFragment : Fragment() {

    private val args: ComposeFragmentArgs by navArgs()
    private val viewModel by activityViewModels<ShareViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MVIHiltTheme {
                    DestinationsNavHost(navGraph = NavGraphs.root)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.shareContact.value = args.data
    }
}

@RootNavGraph(start = true) // 该注解表示根路由页面
@Destination
@Composable
fun FirstScreen(navigator: DestinationsNavigator) {
    val backPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Contact Details") },
                navigationIcon = {
                    Icon(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                backPressedDispatcher?.onBackPressed()
                            },
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            )
        }
    ) {
        val viewModel = hiltViewModel<ShareViewModel>(
            LocalContext.current.findActivity() as ViewModelStoreOwner
        )
        val contactState by viewModel.shareContact.observeAsState()
        FriendDetails(it, contactState) {
            contactState?.let {
                navigator.navigate(MVIScreenDestination(it))
            }
        }
    }
}

@Composable
private fun FriendDetails(
    paddingValues: PaddingValues,
    contactState: Contact?,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(top = 20.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomerItem(
            contactState?.content ?: "NowInGo",
            contactState?.details ?: "Now in android of jetpack"
        )
        Divider(
            modifier = Modifier
                .padding(start = 16.dp, top = 20.dp)
                .fillMaxWidth(),
            color = Color.LightGray
        )
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .height(45.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Tag", style = MaterialTheme.typography.subtitle1)
            Icon(Icons.Filled.KeyboardDoubleArrowRight, contentDescription = null)
        }
        Line()
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .height(45.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "More Information", style = MaterialTheme.typography.subtitle1)
            Icon(Icons.Filled.KeyboardDoubleArrowRight, contentDescription = null)
        }
        Line()
        androidx.compose.material3.OutlinedButton(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 20.dp)
                .fillMaxWidth()
                .semantics { testTag = TestTag.CIRCLEOFFRIENDS },
            onClick = onClick
        ) {
            Text(text = "Circle of friends")
        }
    }
}

@Composable
private fun Line() {
    Divider(
        modifier = Modifier
            .padding(start = 16.dp)
            .fillMaxWidth(),
        color = Color.LightGray
    )
}

@Composable
private fun CustomerItem(name: String, descriptor: String) {
    ConstraintLayout(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        //通过createRefs创建三个引用
        val (imageRef, avatar, nameRef, descRef) = createRefs()
        val guideline = createGuidelineFromTop(fraction = 0.5f)
        Image(
            painter = ColorPainter(colorResource(id = R.color.gray_400)),
            contentDescription = "avatar",
            modifier = Modifier
                .constrainAs(imageRef) {//通过constrainAs将Image与imageRef绑定,并增加约束
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                }
                .size(80.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop)
        Text(
            text = name.first().toString(),
            modifier = Modifier
                .constrainAs(avatar) {
                    top.linkTo(imageRef.top)
                    bottom.linkTo(imageRef.bottom)
                    start.linkTo(imageRef.start)
                    end.linkTo(imageRef.end)
                },
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = name,
            modifier = Modifier
                .constrainAs(nameRef) {
                    bottom.linkTo(guideline)
                    start.linkTo(imageRef.end, 12.dp)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
                .fillMaxWidth(),
            fontSize = 18.sp,
            maxLines = 1,
            textAlign = TextAlign.Left,
            overflow = TextOverflow.Ellipsis,
        )

        Text(text = descriptor, modifier = Modifier
            .constrainAs(descRef) {
                top.linkTo(nameRef.bottom, 5.dp)
                start.linkTo(nameRef.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }
            .fillMaxWidth(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.caption
        )
    }
}

@Destination
@Composable
fun MVIScreen(
    navigator: DestinationsNavigator,
    contact: Contact
) {
    val composeViewModel = hiltViewModel<ComposeViewModel>().apply {
        ComposeAction.LoadData(contact.id).let(::sendAction)
    }
    val scaffoldState = rememberScaffoldState()
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia(maxItems = MAX_COUNT)
    ) {
        composeViewModel.sendAction(ComposeAction.PickImages(it))
    }
    val snackbarHostState = remember { SnackbarHostState() }
    StateEffectScaffold(
        viewModel = composeViewModel,
        sideEffect = { viewModel, sideEffect ->
            when (sideEffect) {
                ComposeEvent.ShowWarring -> {
                    val result = snackbarHostState.showSnackbar(
                        "pick photos",
                        actionLabel = "Go"
                    )
                    when (result) {
                        SnackbarResult.ActionPerformed -> {
                            viewModel.sendAction(ComposeAction.OnActionClicked)
                        }

                        SnackbarResult.Dismissed -> {}
                    }
                }

                ComposeEvent.NavDetails -> {
                    launcher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
            }
        }
    ) { viewModel, state ->
        Scaffold(
            scaffoldState = scaffoldState,
            modifier = Modifier.fillMaxSize(),
            floatingActionButton = {
                if (state is ComposeState.Chat) {
                    FloatingActionButton(
                        onClick = {
                            snackbarHostState.currentSnackbarData?.dismiss()
                            viewModel.sendAction(ComposeAction.OnFABClicked)
                        }
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "")
                    }
                }
            }
        ) {
            when (state) {
                ComposeState.Loading -> Loading()
                ComposeState.Empty -> Empty()
                is ComposeState.Chat -> WeChatMock(it, state, viewModel, snackbarHostState)
            }
        }
    }
}

@Preview
@Composable
private fun Empty() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.mipmap.ic_launcher),
            contentDescription = stringResource(id = R.string.app_name),
            modifier = Modifier
                .size(width = 100.dp, height = 100.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Text("Empty", style = MaterialTheme.typography.caption)
    }
}

@Preview
@Composable
private fun Loading() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(modifier = Modifier.size(40.dp))
        Spacer(modifier = Modifier.height(3.dp))
        Text("Loading", style = MaterialTheme.typography.caption)
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun WeChatMock(
    it: PaddingValues,
    tempState: ComposeState.Chat,
    viewModel: ComposeViewModel,
    snackbarHostState: SnackbarHostState
) {
    Column(
        modifier = Modifier
            .padding(it)
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            userScrollEnabled = false
        ) {
            items(MAX_COUNT) { item ->
                val uri = tempState.uri.getOrNull(item)
                val width = LocalDensity.current.run {
                    ((LocalContext.current.resources.displayMetrics.widthPixels - 48.dp.toPx()) / 3).toDp()
                }
                Image(
                    painter = uri?.let {
                        BitmapPainter(it.decodeBitmap(LocalContext.current).asImageBitmap())
                    } ?: painterResource(id = R.mipmap.ic_launcher),
                    contentDescription = stringResource(id = R.string.app_name),
                    modifier = Modifier
                        .size(width)
                        .border(1.dp, color = MaterialTheme.colors.onSecondary)
                        .combinedClickable(
                            onLongClick = {
                                uri?.let { viewModel.sendAction(ComposeAction.WipeImage(it)) }
                            },
                            onClick = {}
                        ),
                    contentScale = ContentScale.Crop
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = tempState.content,
            modifier = Modifier.animateContentSize(),
            maxLines = if (tempState.expend) Int.MAX_VALUE else 2,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = if (tempState.expend) "收起" else "全文",
            color = Color.Blue,
            modifier = Modifier.clickable {
                viewModel.sendAction(ComposeAction.OnExpendClicked)
            }
        )
    }
    Box(
        modifier = Modifier
            .safeContentPadding()
            .fillMaxWidth(),
        contentAlignment = Alignment.TopCenter //Change to your desired position
    ) {
        SnackbarHost(hostState = snackbarHostState)
    }
}