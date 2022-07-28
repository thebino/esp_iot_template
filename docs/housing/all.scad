use <body.scad>
use <lid.scad>


body(20, 20, 10, 1.2);

// flip
rotate([0,180,0])
translate([-20,0,0])

// positioning on top
translate([0,0,-25.3]) {
    lid(20, 20, 5, 1.8);
}