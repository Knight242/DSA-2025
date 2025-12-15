import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.SwingUtilities
import javax.swing.border.TitledBorder
import java.awt.*
import kotlin.math.roundToInt

class ClusterPanel(
    private val data: List<Point>,
    private val labels: IntArray,
    private val centroids: List<Point>,
    private val colors: Array<Color>,
    private val xLabel: String = "X-axis",
    private val yLabel: String = "Y-axis"
) : JPanel() {

    init {
        preferredSize = Dimension(700, 700)
        background = Color.WHITE
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)

        val g2 = g as Graphics2D
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        if (data.isEmpty()) return

        // Extract X/Y ranges
        val xs = data.map { it.coords[0] }
        val ys = data.map { it.coords[1] }

        val minX = xs.minOrNull()!!
        val maxX = xs.maxOrNull()!!
        val minY = ys.minOrNull()!!
        val maxY = ys.maxOrNull()!!

        val padding = 60
        val innerW = width - 2 * padding
        val innerH = height - 2 * padding

        fun toScreenX(x: Double): Int {
            val t = (x - minX) / (maxX - minX)
            return (padding + t * innerW).roundToInt()
        }

        fun toScreenY(y: Double): Int {
            val t = (y - minY) / (maxY - minY)
            return (height - padding - t * innerH).roundToInt()
        }

        // Grid & axes
        g2.color = Color(230, 230, 230)
        val gridLines = 6
        for (i in 0..gridLines) {
            val gx = padding + (i.toDouble() / gridLines) * innerW
            val gy = padding + (i.toDouble() / gridLines) * innerH

            g2.drawLine(gx.toInt(), padding, gx.toInt(), height - padding)
            g2.drawLine(padding, gy.toInt(), width - padding, gy.toInt())
        }

        g2.color = Color(60, 60, 60)
        g2.drawRect(padding, padding, innerW, innerH)

        // Axis labels
        g2.drawString(xLabel, width / 2 - 20, height - 20)
        g2.drawString(yLabel, 20, height / 2)

        // Ticks
        g2.color = Color(90, 90, 90)
        for (i in 0..gridLines) {
            val xVal = minX + (i.toDouble() / gridLines) * (maxX - minX)
            val yVal = minY + (i.toDouble() / gridLines) * (maxY - minY)

            val xScreen = padding + (i.toDouble() / gridLines) * innerW
            val yScreen = height - padding - (i.toDouble() / gridLines) * innerH

            // X ticks
            g2.drawLine(
                xScreen.toInt(),
                height - padding - 5,
                xScreen.toInt(),
                height - padding + 5
            )
            g2.drawString(
                "%.2f".format(xVal),
                xScreen.toInt() - 10,
                height - padding + 20
            )

            // Y ticks
            g2.drawLine(
                padding - 5,
                yScreen.toInt(),
                padding + 5,
                yScreen.toInt()
            )
            g2.drawString(
                "%.2f".format(yVal),
                padding - 50,
                yScreen.toInt() + 5
            )
        }

        // Making Clusters
        val radius = 8

        for (i in data.indices) {
            val p = data[i]
            val cluster = labels[i] % colors.size

            g2.color = colors[cluster]

            val sx = toScreenX(p.coords[0])
            val sy = toScreenY(p.coords[1])

            g2.fillOval(sx - radius / 2, sy - radius / 2, radius, radius)
        }

        // Draw centroids
        g2.color = Color.BLACK
        g2.stroke = BasicStroke(3f)

        for (c in centroids) {
            val sx = toScreenX(c.coords[0])
            val sy = toScreenY(c.coords[1])

            g2.drawLine(sx - 10, sy, sx + 10, sy)
            g2.drawLine(sx, sy - 10, sx, sy + 10)
        }
    }
}

// Legend for cluster colors and centroid
class LegendPanel(
    private val clusterNames: List<String>,
    private val colors: Array<Color>
) : JPanel() {

    private val k = clusterNames.size

    init {
        preferredSize = Dimension(700, 70)
        background = Color.WHITE
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        val g2 = g as Graphics2D
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        val boxSize = 18
        val spacing = 110
        val startX = 20
        val y = height / 2 - boxSize / 2

        // Cluster color
        for (cluster in 0 until k) {
            val x = startX + cluster * spacing

            g2.color = colors[cluster % colors.size]
            g2.fillOval(x, y, boxSize, boxSize)

            g2.color = Color.BLACK
            g2.drawString(clusterNames[cluster], x + boxSize + 8, y + boxSize - 3)
        }

        // Centroid
        val cx = startX + k * spacing
        g2.stroke = BasicStroke(2.5f)
        g2.color = Color.BLACK
        val centerY = y + boxSize / 2
        g2.drawLine(cx - 8, centerY, cx + 8, centerY)
        g2.drawLine(cx, centerY - 8, cx, centerY + 8)

        g2.drawString("Centroid", cx + 15, y + boxSize - 3)
    }
}

fun showClusterPlot(
    data: List<Point>,
    labels: IntArray,
    centroids: List<Point>,
    title: String = "K-Means Clustering",
    xLabel: String = "X-axis",
    yLabel: String = "Y-axis",
    clusterNames: List<String>? = null
) {
    SwingUtilities.invokeLater {
        val frame = JFrame(title)
        frame.defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
        frame.layout = BorderLayout()

        val colors = arrayOf(
            Color(255, 50, 50, 150),
            Color(50, 120, 255, 150),
            Color(50, 200, 50, 150),
            Color(255, 0, 200, 150),
            Color(255, 150, 0, 150),
            Color(0, 200, 200, 150)
        )

        val k = centroids.size
        val names = clusterNames ?: List(k) { "Cluster $it" }

        val clusterPanel = ClusterPanel(data, labels, centroids, colors, xLabel, yLabel)
        val legendPanel = LegendPanel(names, colors)

        frame.add(clusterPanel, BorderLayout.CENTER)
        frame.add(legendPanel, BorderLayout.SOUTH)

        frame.pack()
        frame.setLocationRelativeTo(null)
        frame.isVisible = true
    }
}

// Side by side (Random vs K-means++)
fun showSideBySideClusterPlots(
    data: List<Point>,
    labelsRandom: IntArray,
    centroidsRandom: List<Point>,
    labelsPlus: IntArray,
    centroidsPlus: List<Point>,
    xLabel: String = "X-axis",
    yLabel: String = "Y-axis",
    clusterNames: List<String>? = null
) {
    SwingUtilities.invokeLater {
        val frame = JFrame("Random init vs K-means++")
        frame.defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
        frame.layout = BorderLayout()

        val colors = arrayOf(
            Color(255, 50, 50, 150),
            Color(50, 120, 255, 150),
            Color(50, 200, 50, 150),
            Color(255, 0, 200, 150),
            Color(255, 150, 0, 150),
            Color(0, 200, 200, 150)
        )

        val centerPanel = JPanel(GridLayout(1, 2))

        val panelRandom = ClusterPanel(
            data,
            labelsRandom,
            centroidsRandom,
            colors,
            xLabel,
            yLabel
        ).apply {
            border = TitledBorder("Random initialization")
        }

        val panelPlus = ClusterPanel(
            data,
            labelsPlus,
            centroidsPlus,
            colors,
            xLabel,
            yLabel
        ).apply {
            border = TitledBorder("K-means++ initialization")
        }

        centerPanel.add(panelRandom)
        centerPanel.add(panelPlus)

        val k = centroidsRandom.size
        val names = clusterNames ?: List(k) { "Cluster $it" }
        val legendPanel = LegendPanel(names, colors)

        frame.add(centerPanel, BorderLayout.CENTER)
        frame.add(legendPanel, BorderLayout.SOUTH)

        frame.pack()
        frame.setLocationRelativeTo(null)
        frame.isVisible = true
    }
}
