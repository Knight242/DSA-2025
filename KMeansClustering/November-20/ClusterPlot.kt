import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.SwingUtilities
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import kotlin.math.roundToInt

class ClusterPanel(
    private val data: List<Point>,
    private val labels: IntArray,
    private val centroids: List<Point>
) : JPanel() {

    init {
        preferredSize = Dimension(600, 600)
        background = Color.WHITE
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)

        if (data.isEmpty()) return

        val xs = data.map { it.coords[0] }
        val ys = data.map { it.coords[1] }

        val minX = xs.minOrNull()!!
        val maxX = xs.maxOrNull()!!
        val minY = ys.minOrNull()!!
        val maxY = ys.maxOrNull()!!

        val padding = 50
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


        g.color = Color(230, 230, 230)
        val gridLines = 6
        for (i in 0..gridLines) {
            val x = padding + (i.toDouble() / gridLines) * innerW
            val y = padding + (i.toDouble() / gridLines) * innerH

            g.drawLine(x.toInt(), padding, x.toInt(), height - padding) // vertical
            g.drawLine(padding, y.toInt(), width - padding, y.toInt())   // horizontal
        }

        // Draw axes
        g.color = Color(80, 80, 80)
        g.drawRect(padding, padding, innerW, innerH)

        // Draw axis labels
        g.drawString("X-axis", width / 2 - 10, height - 10)
        g.drawString("Y-axis", 10, height / 2)

        g.color = Color(90, 90, 90)
        for (i in 0..gridLines) {
            val xVal = minX + (i.toDouble() / gridLines) * (maxX - minX)
            val yVal = minY + (i.toDouble() / gridLines) * (maxY - minY)

            val xScreen = padding + (i.toDouble() / gridLines) * innerW
            val yScreen = height - padding - (i.toDouble() / gridLines) * innerH


            g.drawLine(xScreen.toInt(), height - padding - 5, xScreen.toInt(), height - padding + 5)
            g.drawString("%.1f".format(xVal), xScreen.toInt() - 10, height - padding + 20)


            g.drawLine(padding - 5, yScreen.toInt(), padding + 5, yScreen.toInt())
            g.drawString("%.1f".format(yVal), padding - 40, yScreen.toInt() + 5)
        }

        // Colors for clusters
        val colors = arrayOf(
            Color.RED,
            Color.BLUE,
            Color.GREEN.darker(),
            Color.MAGENTA,
            Color.ORANGE,
            Color.CYAN.darker()
        )

        // Draw points
        for (i in data.indices) {
            val p = data[i]
            val cluster = labels[i]
            g.color = colors[cluster % colors.size]

            val sx = toScreenX(p.coords[0])
            val sy = toScreenY(p.coords[1])
            g.fillOval(sx - 5, sy - 5, 10, 10)
        }

        g.color = Color.BLACK
        for (centroid in centroids) {
            val sx = toScreenX(centroid.coords[0])
            val sy = toScreenY(centroid.coords[1])

            g.drawLine(sx - 10, sy, sx + 10, sy)
            g.drawLine(sx, sy - 10, sx, sy + 10)
        }
    }
}

fun showClusterPlot(
    data: List<Point>,
    labels: IntArray,
    centroids: List<Point>,
    title: String = "K-Means Clustering"
) {
    SwingUtilities.invokeLater {
        val frame = JFrame(title)
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.contentPane.add(ClusterPanel(data, labels, centroids))
        frame.pack()
        frame.setLocationRelativeTo(null)
        frame.isVisible = true
    }
}
