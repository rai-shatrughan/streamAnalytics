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
    const camera = new THREE.PerspectiveCamera( 75, window.innerWidth / window.innerHeight, 0.1, 10000 );
    camera.position.set( 0, 5, 10 );
    camera.lookAt( 0, 0, 0 );

    const renderer = new THREE.WebGLRenderer();
    renderer.setSize( window.innerWidth, window.innerHeight );
    document.getElementById('containerVR').appendChild( renderer.domElement );

    const size = 100;
    const divisions = 50;
    const gridHelper = new THREE.GridHelper( size, divisions );

    const vertices = [];

    for ( let i = 0; i < 10000; i ++ ) {
        const x = THREE.MathUtils.randInt(-1000, 1000);
        const y = THREE.MathUtils.randInt(-1000, 1000);
        const z = THREE.MathUtils.randInt(-1000, 1000);
        vertices.push( x, y, z );
    }

    const geometry = new THREE.BufferGeometry();
    geometry.setAttribute( 'position', new THREE.Float32BufferAttribute( vertices, 3 ) );
    const material = new THREE.PointsMaterial( { color: 0xFFFFFF } );
    const points = new THREE.Points( geometry, material );

    function animate() {
        requestAnimationFrame( animate );

        scene.rotation.x += 0.001;
        scene.rotation.z += 0.001;

        scene.add(points);
        scene.add( gridHelper );
        renderer.render( scene, camera );
    };

    animate();

}

