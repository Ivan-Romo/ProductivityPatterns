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

    modifier: Modifier = Modifier
) {
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
    <div id="chart"></div>

    <!-- Incluir ApexCharts JS -->
    <script src="https://cdn.jsdelivr.net/npm/apexcharts"></script>
    <script>
        // Datos y opciones del gráfico
        const options = {
            series: [{
                name: 'series1',
                data: [31, 40, 28, 51, 42, 109, 100]
            }, {
                name: 'series2',
                data: [11, 32, 45, 32, 34, 52, 41]
            }],
            chart: {
                height: 350,
                type: 'area'
            },
            dataLabels: {
                enabled: false
            },
            stroke: {
                curve: 'smooth'
            },
            xaxis: {
                type: 'datetime',
                categories: [
                    "2018-09-19T00:00:00.000Z", 
                    "2018-09-19T01:30:00.000Z", 
                    "2018-09-19T02:30:00.000Z", 
                    "2018-09-19T03:30:00.000Z", 
                    "2018-09-19T04:30:00.000Z", 
                    "2018-09-19T05:30:00.000Z", 
                    "2018-09-19T06:30:00.000Z"
                ]
            },
            tooltip: {
                x: {
                    format: 'dd/MM/yy HH:mm'
                }
            }
        };

        // Crear el gráfico
        const chart = new ApexCharts(document.querySelector("#chart"), options);
        chart.render();
    </script>
</body>
</html>


    """.trimIndent()

    Box(modifier = modifier.fillMaxWidth().height(250.dp)) {
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
