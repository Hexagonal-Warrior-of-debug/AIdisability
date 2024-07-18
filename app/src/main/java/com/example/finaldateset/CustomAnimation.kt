import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CustomAnimation {
    private val minScaleFactor = 0.5f

    fun applyAnimation(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

        val recyclerViewHeight = recyclerView.height
        val middle = recyclerViewHeight / 2

        for (i in firstVisibleItemPosition..lastVisibleItemPosition) {
            val view = layoutManager.findViewByPosition(i)
            view?.let {
                val viewTop = it.top
                val viewBottom = it.bottom
                val centerY = (viewTop + viewBottom) / 2f
                val maxDistance = recyclerViewHeight / 4f
                val distanceFromCenter = Math.abs(centerY - middle)
                val scaleFactor = 1f - Math.min(distanceFromCenter / maxDistance, 1f)
                val finalScaleFactor = Math.max(scaleFactor, minScaleFactor)
                it.scaleX = finalScaleFactor
                it.scaleY = finalScaleFactor
                it.alpha = finalScaleFactor
            }
        }
    }
}