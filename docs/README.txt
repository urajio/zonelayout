Changes for ZoneLayout 1.0
=================================
-No changes since beta 4


Changes for ZoneLayout 1.0 beta 4
=================================
-Fixed empty container bug.


Changes for ZoneLayout 1.0 beta 3
=================================
-Added fix to layout algorithm for distributed takes on spanning columns
-optimized sizing algorithm
-optimized calls to sizes (pref, min, etc.) on components and the layout itself

Changes for ZoneLayout 1.0 beta 2
=================================
-If a component's min size is not less than or equal to the pref size, ZoneLayout will assume
the min size to be the preferred size instead of throwing an Exception
-Fixed a bug that occurred when a template later in a layout that is smaller than a previous template
adjusted the previous templates coordinates
-Corrected small error in code on cheatsheet
-added small fix for empty panels throwing NPE
