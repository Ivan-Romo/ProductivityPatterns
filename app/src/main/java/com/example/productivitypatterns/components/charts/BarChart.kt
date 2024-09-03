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
import android.webkit.WebView.LAYER_TYPE_HARDWARE
import androidx.compose.foundation.layout.*

@Composable
fun BarChart(
    categories: List<String>,      // Lista para los valores del eje x
    values: List<Int>,             // Lista para los valores del eje y
    barColor: String,              // Color de las barras
    modifier: Modifier = Modifier
) {
    val formattedValues = values.joinToString(", ")
    val formattedCategories = categories.joinToString(", ") { "\"$it\"" }

    val htmlContent = """
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gráfico de Barras</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/apexcharts/dist/apexcharts.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600&display=swap" rel="stylesheet">
    <style>
        body {
            font-family: 'Inter', sans-serif;
            margin: 0;
            background-color: transparent;  /* Fondo transparente */
        }
        #chart {
            margin-top:-1em; 
            margin-left:-1em;
        }
    </style>
</head>
<body>
    <div id="chart"></div>

    <script src="https://cdn.jsdelivr.net/npm/apexcharts"></script>

    <script>
        var options = {
            series: [{
                name: 'Average productivity',
                data: [$formattedValues]
            }],
            chart: {
                height: 250,
                type: 'bar',
                toolbar: {
                    show: false
                },
                background: 'transparent'  // Fondo del gráfico transparente
            },
            plotOptions: {
                bar: {
                    borderRadius: 3,
                    dataLabels: {
                        position: 'top',
                    },
                    columnWidth: '25%',
                }
            },
            colors: ['$barColor'], // Aplicar el color a las columnas
            dataLabels: {
                enabled: false,
                formatter: function (val) {
                    return val + "%";
                },
                offsetY: -30,
                style: {
                    fontSize: '16px',
                    fontFamily: 'Inter, sans-serif',
                    colors: ["#304758"]
                }
            },
            xaxis: {
                categories: [$formattedCategories],
                axisBorder: {
                    show: false
                },
                axisTicks: {
                    show: false
                },
                crosshairs: {
                    fill: {
                        type: 'gradient',
                        gradient: {
                            colorFrom: '$barColor',
                            colorTo: '$barColor',
                            stops: [0, 100],
                            opacityFrom: 0.5,
                            opacityTo: 0.5,
                        }
                    }
                },
                tooltip: {
                    enabled: false,
                },
                labels: {
                    style: {
                        fontFamily: 'Inter, sans-serif',
                    }
                }
            },
            yaxis: {
                axisBorder: {
                    show: false
                },
                axisTicks: {
                    show: false,
                },
                labels: {
                    show: false,
                    formatter: function (val) {
                        return val + "%";
                    },
                    style: {
                        fontFamily: 'Inter, sans-serif',
                    }
                }
            },
        };

        var chart = new ApexCharts(document.querySelector("#chart"), options);
        chart.render();
    </script>
</body>
</html>
    """.trimIndent()

    Box(modifier = modifier.width(300.dp).height(240.dp)) {
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
                    }

                    // Configurar el WebView para que tenga un fondo transparente
                    setBackgroundColor(0x00000000)  // 0x00000000 es transparente
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
