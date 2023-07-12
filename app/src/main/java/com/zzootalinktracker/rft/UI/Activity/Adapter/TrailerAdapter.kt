import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zzootalinktracker.rft.R
import com.zzootalinktracker.rft.UI.Activity.Model.TrailerModel
import de.hdodenhof.circleimageview.CircleImageView

class TrailerAdapter(private val mlist: ArrayList<TrailerModel>) :
    RecyclerView.Adapter<TrailerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvCurrentDate: TextView = itemView.findViewById(R.id.tvCurrentDate)
        private val tvChillerStatus: TextView = itemView.findViewById(R.id.tvChillerStatus)
        private val tvChillerMsg: TextView = itemView.findViewById(R.id.tvChillerMsg)
        private val circularIv: CircleImageView = itemView.findViewById(R.id.circularIv)

        fun bind(item: TrailerModel) {
            tvCurrentDate.text = item.tvCurrentDate
            tvChillerStatus.text = item.tvChillerStatus
            tvChillerMsg.text = item.tvChillerMsg
            val trailerConnectedStatus = item.isBothTrailerConnected

            if (trailerConnectedStatus){
                circularIv.setImageResource(R.drawable.color_green)
                tvChillerStatus.text ="Chiller 1 & Chiller 2 are Connected"
            }else{
                tvChillerStatus.text ="Chiller 1 & Chiller 2 are Not Connected"
                circularIv.setImageResource(R.drawable.solid_red)
            }
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
