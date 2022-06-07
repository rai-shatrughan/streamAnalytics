import React from 'react';
import * as THREE from 'three';

export default class VR extends React.Component {
  
    componentDidMount() {
        drawVR();
    }
  
    render() {
      return (
        <div className="pure-g g4 vr">
            <div className="pure-u-1 pure-u-md-1-2 pure-u-lg-1-4" id="containerVR"></div>
        </div>
      );
    }
}

function drawVR() {
    const scene = new THREE.Scene();
    const camera = new THREE.PerspectiveCamera( 75, window.innerWidth / window.innerHeight, 0.1, 1000 );

    const renderer = new THREE.WebGLRenderer();    
    renderer.setSize( window.innerWidth, window.innerHeight);
    var chartDom = document.getElementById('containerVR');    
    chartDom.appendChild( renderer.domElement );

    const geometry = new THREE.BoxGeometry( 1, 1, 1 );
    const material = new THREE.MeshBasicMaterial( { color: 0x00ff00 } );
    const cube = new THREE.Mesh( geometry, material );
    scene.add( cube );

    camera.position.z = 5;

    function animate() {
        requestAnimationFrame( animate );

        cube.rotation.x += 0.01;
        cube.rotation.y += 0.01;

        renderer.render( scene, camera );
    };

    animate();
    

}