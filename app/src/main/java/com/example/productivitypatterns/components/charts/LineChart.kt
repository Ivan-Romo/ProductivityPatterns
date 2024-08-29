package com.example.productivitypatterns.components.charts

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.os.Build
import android.view.View.LAYER_TYPE_SOFTWARE
import android.webkit.WebView.LAYER_TYPE_HARDWARE
import androidx.compose.foundation.layout.*

@Composable
fun LineChart(
    dataSeries: List<Pair<String, List<Int>>>,  // Lista de pares nombre y datos de la serie
    categories: List<String>,                   // Lista de categorías para el eje X
    colors: List<String>,                       // Lista de colores para las series
    modifier: Modifier = Modifier
) {
// Convertir la lista de series en una representación JSON para ApexCharts
    val seriesJson = dataSeries.mapIndexed { index, (name, data) ->
        """
        {
            name: '$name',
            data: ${data.toString()},
            color: '${colors.getOrNull(index) ?: "#000"}'
        }
        """
    }.joinToString(",")

    // Convertir la lista de categorías en una representación JSON para ApexCharts
    val categoriesJson = categories.joinToString(prefix = "[", postfix = "]") { "\"$it\"" }

    val htmlContent = """
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ApexCharts Example</title>
    <!-- Incluir ApexCharts CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/apexcharts/dist/apexcharts.css">
</head>
<body>
    <div id="chart" style="margin-top:-1em; margin-left:-1em"></div>

    <!-- Incluir ApexCharts JS -->
    <script src="https://cdn.jsdelivr.net/npm/apexcharts"></script>
    <script>
        // Datos y opciones del gráfico
        const options = {
            series: [ $seriesJson ],
            chart: {
                height: 220,
                type: 'area',
                toolbar: {
                    show: false
                }
            },
            dataLabels: {
                enabled: false
            },
            stroke: {
                curve: 'smooth'
            },
            xaxis: {
                type: 'category',  // Cambiado a 'category' para mantener el formato de las categorías
                categories: $categoriesJson
            },
        };

        // Crear el gráfico
        const chart = new ApexCharts(document.querySelector("#chart"), options);
        chart.render();
    </script>
</body>
</html>
    """.trimIndent()


    Box(modifier = modifier.fillMaxWidth().height(210.dp)) {
        AndroidView(
            factory = { context ->
                object : WebView(context) {
                    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
                        // Evita el desplazamiento ignorando los cambios en la posición de scroll
                        scrollTo(0, 0)
                    }
                }.apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        setLayerType(LAYER_TYPE_HARDWARE, null)
                    } else {
                        setLayerType(LAYER_TYPE_SOFTWARE, null)
                    }

                    webViewClient = WebViewClient()
                    webChromeClient = WebChromeClient()
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    settings.loadWithOverviewMode = true

                    isVerticalScrollBarEnabled = false
                    isHorizontalScrollBarEnabled = false

                    settings.useWideViewPort = true
                    settings.cacheMode = WebSettings.LOAD_NO_CACHE
                    loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
                }
            },
            modifier = Modifier.fillMaxWidth().height(400.dp)
        )
    }
}
