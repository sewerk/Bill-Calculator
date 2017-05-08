package pl.srw.billcalculator.history

import android.app.Activity
import android.content.res.Resources
import android.support.v7.widget.Toolbar
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence
import pl.srw.billcalculator.R
import javax.inject.Inject

/**
 * Shows help screen for drawer activity
 */
class HelpHandler @Inject constructor(res: Resources) {

    val fabDesc: String = res.getString(R.string.help_fab_desc)
    val swipeDesc: String = res.getString(R.string.help_swipe_delete_desc)
    val menuDesc: String = res.getString(R.string.help_menu_desc)
    val optionsDesc: String = res.getString(R.string.help_options_desc)

    fun show(activity: Activity) {
        val toolbar = activity.findViewById(R.id.toolbar) as Toolbar
        val fab = activity.findViewById(R.id.fab)
        val swipeIcon = activity.findViewById(R.id.swipe_delete_history_icon)

        TapTargetSequence(activity)
                .targets(
                        TapTarget.forView(fab, fabDesc).transparentTarget(true),
                        TapTarget.forView(swipeIcon, swipeDesc),
                        TapTarget.forToolbarNavigationIcon(toolbar, menuDesc),
                        TapTarget.forToolbarOverflow(toolbar, optionsDesc)
                )
                .continueOnCancel(true)
                .considerOuterCircleCanceled(true)
                .start()
    }
}
