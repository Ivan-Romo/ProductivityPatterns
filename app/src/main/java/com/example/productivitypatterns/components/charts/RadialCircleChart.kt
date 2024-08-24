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
fun RadialCircleChart(
    percentage: Int,
    chartColor: String,
    textColor: String,
    textLabel:String,
    modifier: Modifier = Modifier
) {
    val htmlContent = """
<!DOCTYPE html>
<html lang="es">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Gráfico Radial con ApexCharts</title>
    <link
      href="https://fonts.googleapis.com/css2?family=Inter:wght@400;700&display=swap"
      rel="stylesheet"
    />
    <script src="https://cdn.jsdelivr.net/npm/apexcharts"></script>
    <style>
      body {
          font-family: 'Inter', sans-serif;
      }
      #chart {
          width: 200px;
          height: 200px;
      }
    </style>
  </head>
  <body>
    <div id="chart" style="left:0; margin-left:-45px; margin-top:-20px"></div>
    <script>
      var options = {
          series: [$percentage],
          chart: {
              height: 350,
              type: 'radialBar',
              toolbar: {
                  show: false
              }
          },
          plotOptions: {
              radialBar: {
                  startAngle: -135,
                  endAngle: 225,
                  hollow: {
                      margin: 0,
                      size: '70%',
                      background: '#fff',
                      image: undefined,
                      imageOffsetX: 0,
                      imageOffsetY: 0,
                      position: 'front',
                      dropShadow: {
                          enabled: true,
                          top: 3,
                          left: 0,
                          blur: 4,
                          opacity: 0.24
                      }
                  },
                  track: {
                      background: '#fff',
                      strokeWidth: '67%',
                      margin: 0,
                      dropShadow: {
                          enabled: true,
                          top: -3,
                          left: 0,
                          blur: 4,
                          opacity: 0.35
                      }
                  },
                  dataLabels: {
                      show: true,
                      name: {
                          offsetY: -10,
                          show: true,
                          color: '$textColor',
                          fontSize: '17px',
                          fontFamily: 'Inter, sans-serif'
                      },
                      value: {
                          formatter: function(val) {
                              return parseInt(val);
                          },
                          color: '$textColor',
                          fontSize: '36px',
                          show: true,
                      }
                  }
              }
          },
          fill: {
              type: 'solid',
              colors: ['$chartColor']
          },
          stroke: {
              lineCap: 'round'
          },
          labels: ['$textLabel'],
      };

      var chart = new ApexCharts(document.querySelector("#chart"), options);
      chart.render();
    </script>
  </body>
</html>
    """.trimIndent()

    Box(modifier = modifier.width(130.dp).height(130.dp)) {
        AndroidView(
            factory = { context ->
                WebView(context).apply {
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
                    settings.useWideViewPort = true
                    settings.cacheMode = WebSettings.LOAD_NO_CACHE
                    loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
                }
            },
            modifier = Modifier.fillMaxWidth().height(400.dp)
        )
    }
}
