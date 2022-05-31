import '../styles/index.css';
import 'purecss/build/base-min.css';
import 'purecss/build/grids-min.css';
import 'purecss/build/grids-responsive-min.css';
import $ from "jquery";
import drawMe from './dot';
import drawIntro from './intro';
import drawSkills from './skills';


// $(document).ready( function () {
//   drawMe();
//   draw3d();
// });

$(function(){
  drawIntro();
  drawSkills();
  // drawMe();
});

// document.addEventListener("DOMContentLoaded", function() {
//   drawMe();
// });
