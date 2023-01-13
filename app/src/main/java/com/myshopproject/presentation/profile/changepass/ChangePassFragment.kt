package com.myshopproject.presentation.profile.changepass

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.myshopproject.R
import com.myshopproject.databinding.FragmentChangePassBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePassFragment : Fragment(R.layout.fragment_change_pass) {

    private var _binding: FragmentChangePassBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentChangePassBinding.bind(view)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}