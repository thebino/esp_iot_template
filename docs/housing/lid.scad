/**
 *   Electrical enclosure - Lid v0.1
 *   
 *   Copyright © 2022 Stürmer Benjamin
 *   
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *   
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *   
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
 
 
// number of fragments
$fn = 50;

corner_radius = 1;

module lid(
    width, 
    depth, 
    height, 
    thickness
) {
    difference() {
        /**
         * lid part with rounded corners and flat top
         */
        difference() {
            // 1st part: lid with rouded corners
            translate([
                corner_radius, 
                corner_radius, 
                corner_radius
            ]) {
                color("orange")
                minkowski() {
                    cube([
                        width - corner_radius * 2, 
                        depth - corner_radius * 2, 
                        height - corner_radius * 2 + thickness
                    ], center = false);
                    sphere(corner_radius);
                }
            }
            
            /**
             * 2nd part: flat cut off on top
             */
            translate([0, 0, height]) {
                color("red")
                cube([
                    width, 
                    depth, 
                    thickness
                ]);
            }
        }
        
        /**
         * negative hollow inside of the enclosure
         */
        translate([
            thickness + corner_radius, 
            thickness + corner_radius,
            thickness + corner_radius
        ]) {
            color("green") {
                minkowski() {
                    cube([
                    width - corner_radius * 2 - thickness * 2,
                    depth - corner_radius * 2 - thickness * 2,
                    height - thickness * 2 + thickness
                    ], center = false);
                    sphere(corner_radius);
                }
            }
        }
    }
    
    translate([0, 0, 5]) {
        lid_lip(20, 20, 5, 1.2);
    }
}





/**
 * adds a Lip to the lid part, so it can easily fit into the bottom part.
 *
 * @param {width} The outer width of the lip
 * @param {depth} The outer depth of the lip
 * @param {height} The height of the lip
 * @param {thickness} The thickness of the outer wall
 */
module lid_lip(width, depth, height, thickness) {
    /**
     * bottom part with rounded corners and flat top
     */
    difference() {
        // 1st part: sphere with rounded corners
        translate([
            thickness + corner_radius / 1.5,
            thickness + corner_radius / 1.5, 
            0
        ]) {
            /**
              * Using the minkowski method to combine a square with a circle
              * to achive rounded corners.
              */
            color("yellow")
            minkowski() {
                cube([
                    width - thickness * 1.5 - corner_radius * 2,
                    depth - thickness * 1.5 - corner_radius * 2,
                    height / 4
                ], center = false);
                sphere(corner_radius);
            }
        }
        
        // 2nd part: flat cut off from top of the sphere
        translate([0, 0, thickness]) {
            color("cyan")
            cube([
                width, 
                depth, 
                thickness
            ]);
        }
        
        // 3rd part: flat cut off from bottom of the sph
        translate([0, 0, -thickness]) {
            color("red")
            cube([
                width, 
                depth, 
                thickness
            ]);
        }
        
        translate([
            thickness + corner_radius,
            thickness + corner_radius, 
            0 - thickness
        ]) {
            color("magenta")
            minkowski() {
                cube([
                    width - thickness / 1.5 * 3 - corner_radius * 2,
                    depth - thickness / 1.5 * 3 - corner_radius * 2,
                    height
                ], center = false);
                sphere(corner_radius);
            }
        }
    }       
}

lid(20, 20, 5, 1.2);
