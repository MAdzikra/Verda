//package com.example.verdaapp
//
//
//@Composable
//fun BottomMenuBar(
//    screens: List<NavigationItem>,
//    currentScreen: NavigationItem?,
//    onNavigateTo: (NavigationItem) -> Unit,
//) {
//    val backgroundShape = remember { menuBarShape() }
//
//    Box {
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(56.dp)
//                .background(Color.White, backgroundShape)
//                .align(Alignment.BottomCenter)
//        )
//
//        Column(
//            modifier = Modifier
//                .align(Alignment.TopCenter)
//        ) {
//            FloatingActionButton(
//                shape = RoundedCornerShape(50),
//                containerColor = Color.White,
//                contentColor = Color.Gray,
//                onClick = {},
//                modifier = Modifier.clip(RoundedCornerShape(50))
//            ) {
//                Row(
//                    modifier = Modifier.size(64.dp)
//                ) {
//                    BottomBarItem(screens[2], currentScreen, onNavigateTo)
//                }
//            }
//            Spacer(modifier = Modifier.height(30.dp))
//        }
//
//        Row(
//            modifier = Modifier
//                .height(56.dp)
//                .align(Alignment.BottomCenter)
//        ) {
//            BottomBarItem(screens[0], currentScreen, onNavigateTo)
//            BottomBarItem(screens[1], currentScreen, onNavigateTo)
//
//            Spacer(modifier = Modifier.width(72.dp))
//        }
//    }
//}
//
//@Composable
//private fun RowScope.BottomBarItem(
//    screen: NavigationItem,
//    currentScreen: NavigationItem?,
//    onNavigateTo: (NavigationItem) -> Unit,
//) {
//    val selected = currentScreen?.route == screen.route
//
//    Box(
//        Modifier
//            .selectable(
//                selected = selected,
//                onClick = { onNavigateTo(screen) },
//                role = Role.Tab,
//                interactionSource = remember { MutableInteractionSource() },
//                indication = rememberRipple(radius = 32.dp)
//            )
//            .fillMaxHeight()
//            .weight(1f),
//        contentAlignment = Alignment.Center
//    ) {
//        BadgedBox(
//            badge = {},
//            content = {
//                Image(
//                    painter = painterResource(
//                        id = when {
//                            selected -> screen.selectedIcon
//                            else -> screen.icon
//                        }
//                    ),
//                    contentDescription = null
//                )
//            },
//        )
//    }
//}
//
//private fun menuBarShape() = GenericShape { size, _ ->
//    reset()
//
//    moveTo(0f, 0f)
//
//    val width = 150f
//    val height = 90f
//
//    val point1 = 75f
//    val point2 = 85f
//
//    lineTo(size.width / 2 - width, 0f)
//
//    cubicTo(
//        size.width / 2 - point1, 0f,
//        size.width / 2 - point2, height,
//        size.width / 2, height
//    )
//
//    cubicTo(
//        size.width / 2 + point2, height,
//        size.width / 2 + point1, 0f,
//        size.width / 2 + width, 0f
//    )
//
//    lineTo(size.width / 2 + width, 0f)
//
//    lineTo(size.width, 0f)
//    lineTo(size.width, size.height)
//    lineTo(0f, size.height)
//
//    close()
//}
//
//@Composable
//fun VerdaApp() {
//    var currentScreen by remember { mutableStateOf<NavigationItem?>(null) }
//
//    Box(
//        contentAlignment = Alignment.BottomCenter,
//        modifier = Modifier.fillMaxSize()
//    ) {
//        BottomMenuBar(
//            screens = listOf(
//                NavigationItem(
//                    route = Screen.Home.route,
//                    icon = R.drawable.ic_home,
//                    selectedIcon = R.drawable.ic_home,
//                ),
//                NavigationItem(
//                    route = Screen.Article.route,
//                    icon = R.drawable.ic_article,
//                    selectedIcon = R.drawable.ic_article,
//                ),
//                NavigationItem(
//                    route = Screen.Course.route,
//                    icon = R.drawable.ic_course,
//                    selectedIcon = R.drawable.ic_course,
//                ),
//            ),
//            currentScreen = currentScreen,
//            onNavigateTo = { currentScreen = it },
//        )
//    }
//}