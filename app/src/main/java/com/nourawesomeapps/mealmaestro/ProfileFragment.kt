package com.nourawesomeapps.mealmaestro

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.nourawesomeapps.mealmaestro.auth.AuthActivity
import com.nourawesomeapps.mealmaestro.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var loginSharedPreferences: SharedPreferences
    private lateinit var sharedPreferencesEditor: Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        loginSharedPreferences = requireActivity().getSharedPreferences("IS_LOGGED", Context.MODE_PRIVATE)
        sharedPreferencesEditor = loginSharedPreferences.edit()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = auth.currentUser
        binding.userEmailTv.text = user?.email ?: "Anonymous"

        binding.logoutBtn.setOnClickListener {
            logoutUser()
        }
    }

    private fun logoutUser() {

        val googleSignInClient = context?.let { GoogleSignIn.getClient(it, GoogleSignInOptions.DEFAULT_SIGN_IN) }
        googleSignInClient?.signOut()?.addOnCompleteListener {
            goToAuthActivity()
        }

        auth.signOut()
        goToAuthActivity()
    }

    private fun goToAuthActivity() {
        sharedPreferencesEditor.clear()
        sharedPreferencesEditor.apply()
        startActivity(Intent(activity, AuthActivity::class.java))
        activity?.finish()
    }
}