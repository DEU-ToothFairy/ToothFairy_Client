package com.example.toothfairy.view.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.toothfairy.R
import com.example.toothfairy.adapter.ExpandableFaqAdapter
import com.example.toothfairy.data.Faq
import com.example.toothfairy.databinding.FragmentFaqBinding
import com.example.toothfairy.viewModel.FaqViewModel

class FaqFragment : Fragment() {
    // VARIABLE
    private lateinit var fapVM: FaqViewModel
    private lateinit var bind: FragmentFaqBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        bind = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_faq, container, false)

        bind.faqRecyclerView.setHasFixedSize(true)
        bind.faqRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ExpandableFaqAdapter(loadData())
        }

        return bind.root
    }

    private fun loadData():List<Faq> {
        val faqList = ArrayList<Faq>()

        faqList.add(Faq(
            question = "교정 치료, 왜 필요한가요 ?",
            answer = """ 교정치료를 하는 목적은 크게 두 가지로 말할 수 있습니다. 첫째는 기능의 향상이고, 둘째는 심미적인 향상입니다. 

 즉, 덧니를 교정치료 하면 음식물을 씹을 때 효율이 좋아집니다. 치아교정을 한 후에 얼굴이 많이 예뻐진 사람들을 주위에서 보았을 것입니다.   

 덧니나 뻐드렁니로 인해서 보기 흉하던 입 모양이나 튀어 나온 입 모양을 바꾸어 줄 수 있습니다. 
 
 그 외에도 교정치료는 여러 가지 이유로 해서 필요로 합니다.
            """.trimIndent(),
            isExpanded = true
        ))

        faqList.add(Faq(
            question = "교정 치료는 언제하는게 좋나요 ?",
            answer = """ 학자에 따라 약간씩 견해 차이가 있는 경우도 있지만, 성장중인 아동이 가장 치아 이동이 용이하며, 치료 후에도 잘 적응하므로 재발의 위험이 줄어듭니다. 
 
 물론 성인도 치료가 가능합니다. 그러나 치아의 이동이 느려지고 간혹 재발의 가능성도 있습니다. 

 주의 깊게 치료한다면 교정 치료에 연령 제한은 없다고 말해도 좋습니다.
            """.trimIndent()
        ))

        faqList.add(Faq(
            question = "교정 치료는 아픈가요 ?",
            answer = """ 학자에 따라 약간씩 견해 차이가 있는 경우도 있지만, 성장중인 아동이 가장 치아 이동이 용이하며, 치료 후에도 잘 적응하므로 재발의 위험이 줄어듭니다. 
 
 물론 성인도 치료가 가능합니다. 그러나 치아의 이동이 느려지고 간혹 재발의 가능성도 있습니다. 

 주의 깊게 치료한다면 교정 치료에 연령 제한은 없다고 말해도 좋습니다.
            """.trimIndent()
        ))

        faqList.add(Faq(
            question = "교정 치료는 얼마나 걸리나요 ?",
            answer = """ 학자에 따라 약간씩 견해 차이가 있는 경우도 있지만, 성장중인 아동이 가장 치아 이동이 용이하며, 치료 후에도 잘 적응하므로 재발의 위험이 줄어듭니다. 
 
 물론 성인도 치료가 가능합니다. 그러나 치아의 이동이 느려지고 간혹 재발의 가능성도 있습니다. 

 주의 깊게 치료한다면 교정 치료에 연령 제한은 없다고 말해도 좋습니다.
            """.trimIndent()
        ))

        faqList.add(Faq(
            question = "교정 장치는 어떤 종류가 있나요 ?",
            answer = """ 학자에 따라 약간씩 견해 차이가 있는 경우도 있지만, 성장중인 아동이 가장 치아 이동이 용이하며, 치료 후에도 잘 적응하므로 재발의 위험이 줄어듭니다. 
 
 물론 성인도 치료가 가능합니다. 그러나 치아의 이동이 느려지고 간혹 재발의 가능성도 있습니다. 

 주의 깊게 치료한다면 교정 치료에 연령 제한은 없다고 말해도 좋습니다.
            """.trimIndent()
        ))

        return faqList
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fapVM = ViewModelProvider(this)[FaqViewModel::class.java]

        // TODO: Use the ViewModel
    }

    companion object {
        fun newInstance() = FaqFragment()
    }
}