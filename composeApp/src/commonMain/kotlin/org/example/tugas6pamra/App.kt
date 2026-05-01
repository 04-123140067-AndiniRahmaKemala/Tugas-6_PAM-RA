package org.example.tugas6pamra

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import coil3.compose.AsyncImage
import org.example.tugas6pamra.data.Article
import org.example.tugas6pamra.ui.NewsUiState
import org.example.tugas6pamra.ui.NewsViewModel

@Composable
fun App() {
    var isDarkTheme by remember { mutableStateOf(true) }
    
    val colorScheme = if (isDarkTheme) {
        darkColorScheme(
            primary = Color(0xFFD0BCFF),
            background = Color(0xFF0C0B0F),
            surface = Color(0xFF1C1B1F)
        )
    } else {
        lightColorScheme(
            primary = Color(0xFF6750A4),
            background = Color(0xFFFDFBFF),
            surface = Color(0xFFFFFFFF)
        )
    }

    MaterialTheme(colorScheme = colorScheme) {
        val navController = rememberNavController()
        val viewModel: NewsViewModel = viewModel { NewsViewModel() }
        
        Scaffold(
            bottomBar = { EliteBottomBar(navController) }
        ) { padding ->
            NavHost(
                navController = navController, 
                startDestination = "discover",
                modifier = Modifier.padding(padding)
            ) {
                composable("discover") {
                    NewsListScreen(viewModel, isDarkTheme, onThemeToggle = { isDarkTheme = !isDarkTheme }) { index ->
                        navController.navigate("article_detail/$index")
                    }
                }
                composable("bookmarks") {
                    BookmarkScreen(viewModel) { index ->
                        navController.navigate("article_detail/$index")
                    }
                }
                composable(
                    "article_detail/{articleIndex}",
                    arguments = listOf(navArgument("articleIndex") { type = NavType.IntType })
                ) { backStackEntry ->
                    val articleIndex = backStackEntry.arguments?.getInt("articleIndex") ?: -1
                    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                    if (uiState is NewsUiState.Success) {
                        val articles = (uiState as NewsUiState.Success).articles
                        if (articleIndex in articles.indices) {
                            ArticleDetailScreen(articles[articleIndex], viewModel) {
                                navController.popBackStack()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EliteBottomBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Surface(
        color = MaterialTheme.colorScheme.background.copy(alpha = 0.85f),
        modifier = Modifier.clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)),
        tonalElevation = 8.dp
    ) {
        NavigationBar(containerColor = Color.Transparent) {
            NavigationBarItem(
                icon = { Icon(Icons.Default.Home, null) },
                label = { Text("Discover", fontWeight = FontWeight.Bold) },
                selected = currentDestination?.hierarchy?.any { it.route == "discover" } == true,
                onClick = { navController.navigate("discover") { popUpTo("discover") { inclusive = true } } }
            )
            NavigationBarItem(
                icon = { Icon(Icons.Default.Favorite, null) },
                label = { Text("Saved", fontWeight = FontWeight.Bold) },
                selected = currentDestination?.hierarchy?.any { it.route == "bookmarks" } == true,
                onClick = { navController.navigate("bookmarks") }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsListScreen(viewModel: NewsViewModel, isDark: Boolean, onThemeToggle: () -> Unit, onArticleClick: (Int) -> Unit) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentCategory by viewModel.currentCategory.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val bookmarks by viewModel.bookmarks.collectAsStateWithLifecycle()
    var isSearchActive by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            if (isSearchActive) {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { viewModel.onSearchQueryChanged(it) },
                    onSearch = { viewModel.fetchNews(query = it); isSearchActive = false },
                    active = true,
                    onActiveChange = { if (!it) isSearchActive = false },
                    placeholder = { Text("Search stories...") },
                    leadingIcon = { Icon(Icons.Default.Search, null) },
                    trailingIcon = { IconButton(onClick = { isSearchActive = false }) { Icon(Icons.Default.Close, null) } },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(24.dp)
                ) { }
            } else {
                LargeTopAppBar(
                    title = { 
                        Column {
                            Text("INSIGHT", fontWeight = FontWeight.Black, letterSpacing = 2.sp)
                            Text(getGreeting(), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                        }
                    },
                    actions = {
                        IconButton(onClick = { isSearchActive = true }) { Icon(Icons.Default.Search, null) }
                        IconButton(onClick = onThemeToggle) { 
                            Icon(if (isDark) Icons.Default.LightMode else Icons.Default.DarkMode, null) 
                        }
                    },
                    colors = TopAppBarDefaults.largeTopAppBarColors(containerColor = Color.Transparent)
                )
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    CategoryChip(selected = currentCategory == null, label = "Trending") { viewModel.fetchNews(category = null) }
                }
                items(viewModel.categories) { category ->
                    CategoryChip(selected = currentCategory == category, label = category) { viewModel.fetchNews(category = category) }
                }
            }

            AnimatedContent(targetState = uiState) { state ->
                PullToRefreshBox(
                    isRefreshing = state is NewsUiState.Loading,
                    onRefresh = { viewModel.fetchNews() },
                    modifier = Modifier.fillMaxSize()
                ) {
                    when (state) {
                        is NewsUiState.Loading -> LoadingSkeleton()
                        is NewsUiState.Success -> {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(24.dp)
                            ) {
                                if (state.articles.isNotEmpty()) {
                                    item { HeroCarousel(state.articles.take(5), onArticleClick) }
                                }
                                itemsIndexed(state.articles.drop(5)) { index, article ->
                                    ModernNewsItem(article, bookmarks.contains(article.url), { viewModel.toggleBookmark(it) }) {
                                        onArticleClick(index + 5)
                                    }
                                }
                            }
                        }
                        is NewsUiState.Error -> ErrorState(state.message) { viewModel.fetchNews() }
                    }
                }
            }
        }
    }
}

@Composable
fun HeroCarousel(articles: List<Article>, onClick: (Int) -> Unit) {
    val pagerState = rememberPagerState(pageCount = { articles.size })
    Box(modifier = Modifier.fillMaxWidth().height(320.dp).clip(RoundedCornerShape(32.dp))) {
        HorizontalPager(state = pagerState) { page ->
            Box(modifier = Modifier.fillMaxSize().clickable { onClick(page) }) {
                AsyncImage(model = articles[page].urlToImage, contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f)))))
                Column(modifier = Modifier.align(Alignment.BottomStart).padding(20.dp)) {
                    Surface(color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(8.dp)) {
                        Text(articles[page].source.name.uppercase(), Modifier.padding(8.dp, 4.dp), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black)
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(articles[page].title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = Color.White, maxLines = 2)
                }
            }
        }
        Row(Modifier.align(Alignment.BottomEnd).padding(20.dp), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            repeat(articles.size) { i ->
                Box(modifier = Modifier.size(if (pagerState.currentPage == i) 12.dp else 6.dp, 6.dp).clip(CircleShape).background(if (pagerState.currentPage == i) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.5f)))
            }
        }
    }
}

@Composable
fun BookmarkScreen(viewModel: NewsViewModel, onArticleClick: (Int) -> Unit) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val bookmarks by viewModel.bookmarks.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Saved Articles", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
        Spacer(Modifier.height(20.dp))
        
        if (bookmarks.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Your bookmarks will appear here.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else if (uiState is NewsUiState.Success) {
            val allArticles = (uiState as NewsUiState.Success).articles
            val savedIndices = allArticles.mapIndexedNotNull { index, article -> if (bookmarks.contains(article.url)) index to article else null }
            LazyColumn(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                items(savedIndices) { (index, article) ->
                    ModernNewsItem(article, true, { viewModel.toggleBookmark(it) }) { onArticleClick(index) }
                }
            }
        }
    }
}

@Composable
fun ModernNewsItem(article: Article, isBookmarked: Boolean, onBookmark: (String) -> Unit, onClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().clickable { onClick() }, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        AsyncImage(model = article.urlToImage, contentDescription = null, modifier = Modifier.size(100.dp).clip(RoundedCornerShape(20.dp)), contentScale = ContentScale.Crop)
        Column(modifier = Modifier.weight(1f)) {
            Text(article.source.name, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            Text(article.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, maxLines = 2, overflow = TextOverflow.Ellipsis)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.DateRange, null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(" " + article.publishedAt.take(10), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { onBookmark(article.url) }) {
                    Icon(if (isBookmarked) Icons.Default.Favorite else Icons.Default.FavoriteBorder, null, tint = if (isBookmarked) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleDetailScreen(article: Article, viewModel: NewsViewModel, onBack: () -> Unit) {
    val uriHandler = LocalUriHandler.current
    val bookmarks by viewModel.bookmarks.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { uriHandler.openUri(article.url) },
                icon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, null) },
                text = { Text("Open Browser") }
            )
        }
    ) { _ ->
        Column(modifier = Modifier.fillMaxSize().verticalScroll(scrollState)) {
            Box(modifier = Modifier.fillMaxWidth().height(400.dp)) {
                AsyncImage(model = article.urlToImage, contentDescription = null, modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp)), contentScale = ContentScale.Crop)
                val progress = if (scrollState.maxValue > 0) scrollState.value.toFloat() / scrollState.maxValue else 0f
                LinearProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxWidth().height(4.dp).align(Alignment.TopCenter), color = MaterialTheme.colorScheme.primary, trackColor = Color.Transparent)
                Row(modifier = Modifier.padding(top = 48.dp, start = 20.dp, end = 20.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    FilledIconButton(onClick = onBack, colors = IconButtonDefaults.filledIconButtonColors(containerColor = Color.Black.copy(alpha = 0.5f))) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White) }
                    FilledIconButton(onClick = { viewModel.toggleBookmark(article.url) }, colors = IconButtonDefaults.filledIconButtonColors(containerColor = Color.Black.copy(alpha = 0.5f))) { 
                        Icon(if (bookmarks.contains(article.url)) Icons.Default.Favorite else Icons.Default.FavoriteBorder, null, tint = if (bookmarks.contains(article.url)) Color.Red else Color.White) 
                    }
                }
            }
            Column(modifier = Modifier.padding(24.dp)) {
                Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(8.dp)) {
                    Text(article.source.name, Modifier.padding(8.dp, 4.dp), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(16.dp))
                Text(text = article.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
                Spacer(Modifier.height(24.dp))
                Text(text = article.description ?: "", style = MaterialTheme.typography.bodyLarge, lineHeight = 30.sp)
                article.content?.let { Spacer(Modifier.height(16.dp)); Text(it.substringBefore("[+"), style = MaterialTheme.typography.bodyLarge) }
                Spacer(Modifier.height(100.dp))
            }
        }
    }
}

@Composable
fun CategoryChip(selected: Boolean, label: String, onClick: () -> Unit) {
    FilterChip(selected = selected, onClick = onClick, label = { Text(label) }, shape = RoundedCornerShape(16.dp))
}

@Composable
fun LoadingSkeleton() {
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(24.dp)) {
        Box(modifier = Modifier.fillMaxWidth().height(280.dp).clip(RoundedCornerShape(32.dp)).background(Color.Gray.copy(alpha = 0.3f)))
        repeat(3) { Box(modifier = Modifier.fillMaxWidth().height(100.dp).clip(RoundedCornerShape(20.dp)).background(Color.Gray.copy(alpha = 0.2f))) }
    }
}

@Composable
fun ErrorState(message: String, onRetry: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Warning, null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.error)
            Text("Connection Error", fontWeight = FontWeight.Bold)
            Text(message, textAlign = TextAlign.Center)
            Button(onClick = onRetry) { Text("Retry") }
        }
    }
}

fun getGreeting(): String {
    // Simulasi greeting sederhana
    return "Stay informed today"
}
