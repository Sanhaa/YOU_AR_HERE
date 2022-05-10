package com.example.myapplication

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.SceneView
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.gorisse.thomas.sceneform.scene.await
//
class MainFragment : Fragment(R.layout.fragment_main) {
    val TAG = "SANA"

    lateinit var btn_add: Button
    var targetMsg = "this is target message"

    private lateinit var arFragment: ArFragment
    private val arSceneView get() = arFragment.arSceneView
    private val scene get() = arSceneView.scene

    private var model: Renderable? = null
    private var modelView: ViewRenderable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inputfragment가 보낸 데이터를 받기 위해
        // FragmentManager에서 리스너 API 호출
//        parentFragmentManager.setFragmentResultListener(
//            "requestKey", this
//        ) { key, bundle ->
//            // We use a String here, but any type that can be put in a Bundle is supported
//            result = bundle.getString("bundleKey")!!
//            // Do something with the result...
//            Log.d("SANHA", "MainFragment onCreate setFragmentResultListener — get result $result");
//            Toast.makeText(context, "get : " + result, Toast.LENGTH_SHORT).show()
//        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_add = view.findViewById(R.id.addButton)
        btn_add.setOnClickListener{
            val dialog = InputDialog()
            dialog.setOkListener(this::onConfirmPressed)
            dialog.show(childFragmentManager, "Message")
        }

//        arFragment = (childFragmentManager.findFragmentById(R.id.arFragment) as ArFragment).apply {
//            setOnSessionConfigurationListener { session, config ->
//                // Modify the AR session configuration here
//            }
//            setOnViewCreatedListener { arSceneView ->
//                arSceneView.setFrameRateFactor(SceneView.FrameRate.FULL)
//            }
//            setOnTapArPlaneListener(::onTapPlane)
//        }
//
//        lifecycleScope.launchWhenCreated {
//            loadModels()
//        }

    }

    fun onConfirmPressed(dialogVal: String) {
        targetMsg = dialogVal
        Log.d(TAG, "targetMsg = $targetMsg")

        Toast.makeText(context, "targetMsg = $targetMsg", Toast.LENGTH_LONG).show()

        arFragment = (childFragmentManager.findFragmentById(R.id.arFragment) as ArFragment).apply {
            setOnSessionConfigurationListener { session, config ->
                // Modify the AR session configuration here
            }
            setOnViewCreatedListener { arSceneView ->
                arSceneView.setFrameRateFactor(SceneView.FrameRate.FULL)
            }
            setOnTapArPlaneListener(::onTapPlane)
        }
        lifecycleScope.launchWhenCreated {
            loadModels()
        }
    }

    private suspend fun loadModels() {
        model = ModelRenderable.builder()
            .setSource(context, Uri.parse("models/scene.gltf"))
            .setIsFilamentGltf(true)
            .await()
        modelView = ViewRenderable.builder()
            .setView(context, R.layout.view_renderable_infos)
            .await()
    }

    private fun onTapPlane(hitResult: HitResult, plane: Plane, motionEvent: MotionEvent) {
        if (model == null || modelView == null) {
            Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT).show()
            return
        }

        // tap 했을 때 input 하도록 하려면
//        val dialog = InputDialog()
//        dialog.setOkListener(this::onConfirmPressed)
//        dialog.show(supportFragmentManager, "Resolving")

        // set text to 'result' from InputFragment
        val v = modelView
        val tv:TextView? = v?.view?.findViewById<TextView>(R.id.messageTextView)

        // Create the Anchor.
        scene.addChild(AnchorNode(hitResult.createAnchor()).apply {
            // Create the transformable model and add it to the anchor.
//            addChild(TransformableNode(arFragment.transformationSystem).apply {
                localScale = Vector3(0.1f, 0.1f, 0.1f)
                renderable = model
//                renderableInstance.animate(true).start()
//                // Add the View11
                addChild(Node().apply {
                    // Define the relative position
                    localPosition = Vector3(0.0f, 3f, 0.0f)
                    localScale = Vector3(10f, 10f, 10f)
                    renderable = modelView

                    if (tv!=null){
                        tv.text = targetMsg
                    }
                    else{
                        Log.d("SANHA", "MainFragment onTapPlane - tv is null")
                    }
                })
//            })
        })
    }
}