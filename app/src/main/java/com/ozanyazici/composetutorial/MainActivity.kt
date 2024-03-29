package com.ozanyazici.composetutorial

import SampleData
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ozanyazici.composetutorial.ui.theme.ComposeTutorialTheme
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
// ComposetutorialTheme ve Surface olmadanda yazılabilir ama kullanılırsa
// composable'larınız, uygulamanızın temasında tanımlanan stilleri devralarak uygulamanız genelinde tutarlılık sağlayabilir.
// Telefonun temasına göre uygulamanın renklerinin değişmemesi için ui.theme paketinin
// içindeki Theme.kt dosyasında dynamicColor false olarak ayarlanabilir.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTutorialTheme {
                Surface {
                    Conversation(messages = SampleData.conversationSample)
                }
            }
        }
    }
}

data class Message(val author: String, val body: String)

@Composable
fun MessageCard(msg: Message) {
    Row {
        Image(painter = painterResource(id = R.drawable.profile_picture),
            contentDescription = "Contact profile picture",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.primary,)
        )

        Spacer(modifier = Modifier.width(8.dp))
        // rememberSaveable fonksiyonu, remember fonksiyonuna benzer bir şekilde değer saklar,
        // ancak bu değerin yaşam döngüsü daha kalıcıdır. Örneğin, ekran rotasyonu gibi durumlarda
        // bile bu değer saklanır ve tekrar yüklenir.
        var isExpanded by remember { mutableStateOf(false) }
        // bir renk değerini değiştirdiğinizde, bu değişikliği animasyonlu bir şekilde
        // yapan bir durum değeri (State) üretir.
        val surfaceColor by animateColorAsState(
            if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
        )

        Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
            Text(
                text = msg.author,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                shape = MaterialTheme.shapes.medium,
                shadowElevation = 1.dp,
                color = surfaceColor,
                // animateContentSize, bir bileşenin içeriği değiştiğinde otomatik olarak
                // boyutunu animasyonlu bir şekilde ayarlayan bir fonksiyondur.
                modifier = Modifier.animateContentSize().padding(1.dp)
            ) {
                Text(
                    text = msg.body,
                    modifier = Modifier.padding(all = 4.dp),
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun Conversation(messages: List<Message>) {
    LazyColumn {
        items(messages) { message ->
            MessageCard(msg = message)
        }
    }
}

@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)

@Preview
@Composable
fun PreviewMessageCard() {
    ComposeTutorialTheme {
        Surface {
            Conversation(messages = SampleData.conversationSample)
        }
    }
}
