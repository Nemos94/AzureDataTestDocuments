/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.pcgomes.azuredatatestdocuments.Animations;

import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.pcgomes.azuredatatestdocuments.R;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.SkeletonNode;
import com.google.ar.sceneform.animation.ModelAnimator;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.AnimationData;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;

/** Demonstrates playing animated FBX models. */
public class Inicialize extends AppCompatActivity {

  private static final String TAG = "AnimationSample";
  private static final int ANDY_RENDERABLE = 1;
  private static final int ANDY_DANCE = 2;
 // private static final int HAT_RENDERABLE = 3;
  private static final String HAT_BONE_NAME = "hat_point";
  private static final String HAT_BONE_NAME2 = "hat_point2";
  private ArFragment arFragment;
  // Model loader class to avoid leaking the activity context.
  private ModelLoader modelLoader;
  private ModelRenderable andyRenderable;
    private ModelRenderable andyRenderable2;
  private AnchorNode anchorNode;
  private SkeletonNode andy;
  private SkeletonNode andy2;
  // Controls animation playback.
  private ModelAnimator animator;
  // Index of the current animation playing.
  private int nextAnimation;
  // The UI to play next animation.
  private FloatingActionButton animationButton;
  private FloatingActionButton animationButton2;
  // The UI to toggle wearing the hat.
  private FloatingActionButton hatButton;
  private Node hatNode;
  private ModelRenderable hatRenderable;

  @RequiresApi(api = Build.VERSION_CODES.N)
  @Override
  @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.animations);

    arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.sceneform_fragment);

    modelLoader = new ModelLoader(this);

    modelLoader.loadModel(ANDY_RENDERABLE, R.raw.assemblymachine62);
   //modelLoader.loadModel(ANDY_DANCE, R.raw.andy_dance);
    //modelLoader.loadModel(HAT_RENDERABLE, R.raw.baseball_cap);

    // When a plane is tapped, the model is placed on an Anchor node anchored to the plane.
    arFragment.setOnTapArPlaneListener(this::onPlaneTap);

    // Add a frame update listener to the scene to control the state of the buttons.
    arFragment.getArSceneView().getScene().addOnUpdateListener(this::onFrameUpdate);

    // Once the model is placed on a plane, this button plays the animations.
    animationButton = findViewById(R.id.animate2);
    animationButton.setEnabled(false);
    animationButton.setOnClickListener(v -> {onPlayAnimation(this.getCurrentFocus(),andyRenderable);});

    // Place or remove a hat on Andy's head showing how to use Skeleton Nodes.
    hatButton = findViewById(R.id.hat);
    hatButton.setEnabled(false);
    hatButton.setOnClickListener(this::onToggleHat);
  }

  @RequiresApi(api = Build.VERSION_CODES.N)
  private void onPlayAnimation(View unusedView, ModelRenderable andymodel) {
     // nextAnimation = 0;
    if (animator == null || !animator.isRunning()) {

      AnimationData data = andymodel.getAnimationData(nextAnimation);
      nextAnimation = (nextAnimation + 1) % andymodel.getAnimationDataCount();
      animator = new ModelAnimator(data, andymodel);
      animator.start();
      Toast toast = Toast.makeText(this, data.getName(), Toast.LENGTH_SHORT);
      //Toast toast2 = Toast.makeText(this, String.valueOf(andymodel.getAnimationDataCount()), Toast.LENGTH_SHORT);
      Log.d(
          TAG,
          String.format(
              "Starting animation %s - %d ms long", data.getName(), data.getDurationMs()));
      toast.setGravity(Gravity.CENTER, 0, 0);
     // toast2.setGravity(Gravity.CENTER, 0, 0);
      toast.show();
      //toast2.show();
    }
  }

  /*
   * Used as the listener for setOnTapArPlaneListener.
   */
  private void onPlaneTap(HitResult hitResult, Plane unusedPlane, MotionEvent unusedMotionEvent) {
    if (andyRenderable == null ) {
      return;
    }
    // Create the Anchor.
    Anchor anchor = hitResult.createAnchor();

    if (anchorNode == null) {
      anchorNode = new AnchorNode(anchor);
      anchorNode.setParent(arFragment.getArSceneView().getScene());
      anchorNode.setLocalRotation(Quaternion.axisAngle(new Vector3(0, 1f, 1f), 180));
      andy = new SkeletonNode();
      andy2 = new SkeletonNode();

      andy.setParent(anchorNode);
      andy.setRenderable(andyRenderable);
      andy.setLocalRotation(Quaternion.axisAngle(new Vector3(0, 1f, 0), 180));;
      hatNode = new Node();

      // Attach a node to the bone.  This node takes the internal scale of the bone, so any
      // renderables should be added to child nodes with the world pose reset.
      // This also allows for tweaking the position relative to the bone.
      Node boneNode = new Node();
      boneNode.setParent(andy);
      //boneNode.setParent(andy2);
      andy.setBoneAttachment(HAT_BONE_NAME, boneNode);
     // andy2.setBoneAttachment(HAT_BONE_NAME2, boneNode);
      hatNode.setRenderable(hatRenderable);
      hatNode.setParent(boneNode);
      hatNode.setWorldScale(Vector3.one());
      hatNode.setWorldRotation(Quaternion.identity());
        hatNode.setLocalRotation(Quaternion.axisAngle(new Vector3(0, 1f, 0), 180));;
      Vector3 pos = hatNode.getWorldPosition();

      // Lower the hat down over the antennae.
      pos.y -= .1f;

      hatNode.setWorldPosition(pos);
    }
  }
  /**
   * Called on every frame, control the state of the buttons.
   *
   * @param unusedframeTime
   */
  private void onFrameUpdate(FrameTime unusedframeTime) {
      if (anchorNode == null) {
          if (animationButton.isEnabled()) {
              animationButton.setBackgroundTintList(ColorStateList.valueOf(android.graphics.Color.GRAY));
              animationButton.setEnabled(false);
              hatButton.setBackgroundTintList(ColorStateList.valueOf(android.graphics.Color.GRAY));
              hatButton.setEnabled(false);
          }
      } else {
          if (!animationButton.isEnabled()) {
              animationButton.setBackgroundTintList(
                      ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorAccent)));
              animationButton.setEnabled(true);
              hatButton.setEnabled(true);
              hatButton.setBackgroundTintList(
                      ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimary)));
          }
      }
  }

  private void onToggleHat(View unused) {
    if (hatNode != null) {
      hatNode.setEnabled(!hatNode.isEnabled());

      // Set the state of the hat button based on the hat node.
      if (hatNode.isEnabled()) {
        hatButton.setBackgroundTintList(
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorPrimary)));
      } else {
        hatButton.setBackgroundTintList(
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorAccent)));
      }
    }
  }

  void setRenderable(int id, ModelRenderable renderable) {
    if (id == ANDY_RENDERABLE) {
      this.andyRenderable = renderable;
    } else {
     // this.andyRenderable2 = renderable;
    }

  }

  void onException(int id, Throwable throwable) {
    Toast toast = Toast.makeText(this, "Unable to load renderable: " + id, Toast.LENGTH_LONG);
    toast.setGravity(Gravity.CENTER, 0, 0);
    toast.show();
    Log.e(TAG, "Unable to load andy renderable", throwable);
  }
}
