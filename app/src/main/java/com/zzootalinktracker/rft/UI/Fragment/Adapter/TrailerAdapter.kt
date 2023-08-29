import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zzootalinktracker.rft.R
import com.zzootalinktracker.rft.UI.Activity.Model.TrailerModel
import com.zzootalinktracker.rft.UI.Fragment.Model.GetTagsStatusHistoryModel
import com.zzootalinktracker.rft.Utils.convertTimeToRFt
import de.hdodenhof.circleimageview.CircleImageView

class TrailerAdapter(private val mlist: ArrayList<GetTagsStatusHistoryModel.Data>) :
    RecyclerView.Adapter<TrailerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        private val tvMessage: TextView = itemView.findViewById(R.id.tvMessage)
        private val tvdDateTimeInUserTimeZone: TextView =
            itemView.findViewById(R.id.tvddateTimeInUserTimeZone)

        fun bind(item: GetTagsStatusHistoryModel.Data) {
            var status = item.status
            var message = item.message
            var dateTimeUserTimeZone = item.dateTimeInUserTimeZone
            tvStatus.text = status
            if(message!=null){
                tvMessage.text = message
            }
           // tvDateTimeInUtcTime.text = convertTimeToRFt(dateTimeUTC)
            tvdDateTimeInUserTimeZone.text = convertTimeToRFt(dateTimeUserTimeZone)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.trailer_item_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mlist[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return mlist.size
    }
}
