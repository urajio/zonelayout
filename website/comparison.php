<?php

function get_page_title() {
    return "ZoneLayout.com Comparison";
}

function render_page() {
?>

<h2>ZoneLayout vs. GridBagLayout vs. FormLayout</h2>

<p>
    In this quick comparison, we'll recreate the behavior of BorderLayout using 3 different layout
    managers.  The full listing is <a href="com/atticlabs/zonelayout/swing/examples/BorderLayoutComparison.java.html">here</a>
    and here is the final result:
</p>

<div align="center">
    <img src="images/borderLayoutComparison.png" alt="BorderLayout Comparison Window"/>
</div>

<p>
    Here's the GridBagLayout code that creates the necessary layout:
</p>

<style type="text/css">
.ln { color: rgb(0,0,0); font-weight: normal; font-style: normal; }
.s0 { color: rgb(0,0,128); font-weight: bold; }
.s1 { color: rgb(0,0,0); }
.s2 { color: rgb(0,128,0); font-weight: bold; }
.s3 { color: rgb(0,0,255); }
</style>

<pre>
<span class="s1">
<a name="l51"><span class="ln">51   </span></a>        JPanel p = </span><span class="s0">new </span><span class="s1">JPanel(</span><span class="s0">new </span><span class="s1">GridBagLayout());
<a name="l52"><span class="ln">52   </span></a>        GridBagConstraints gc = </span><span class="s0">new </span><span class="s1">GridBagConstraints();
<a name="l53"><span class="ln">53   </span></a>        gc.gridx = </span><span class="s3">0</span><span class="s1">; gc.gridy = </span><span class="s3">0</span><span class="s1">;
<a name="l54"><span class="ln">54   </span></a>        gc.gridwidth = </span><span class="s3">3</span><span class="s1">; gc.fill = GridBagConstraints.HORIZONTAL;
<a name="l55"><span class="ln">55   </span></a>        p.add(north, gc);
<a name="l56"><span class="ln">56   </span></a>        gc.gridx = </span><span class="s3">0</span><span class="s1">; gc.gridy = </span><span class="s3">1</span><span class="s1">;
<a name="l57"><span class="ln">57   </span></a>        gc.gridwidth = </span><span class="s3">1</span><span class="s1">; gc.fill = GridBagConstraints.VERTICAL;
<a name="l58"><span class="ln">58   </span></a>        p.add(west, gc);
<a name="l59"><span class="ln">59   </span></a>        gc.gridx = </span><span class="s3">2</span><span class="s1">; gc.gridy = </span><span class="s3">1</span><span class="s1">;
<a name="l60"><span class="ln">60   </span></a>        gc.gridwidth = </span><span class="s3">1</span><span class="s1">; gc.fill = GridBagConstraints.VERTICAL;
<a name="l61"><span class="ln">61   </span></a>        p.add(east, gc);
<a name="l62"><span class="ln">62   </span></a>        gc.gridx = </span><span class="s3">0</span><span class="s1">; gc.gridy = </span><span class="s3">2</span><span class="s1">;
<a name="l63"><span class="ln">63   </span></a>        gc.gridwidth = </span><span class="s3">3</span><span class="s1">; gc.fill = GridBagConstraints.HORIZONTAL;
<a name="l64"><span class="ln">64   </span></a>        p.add(south, gc);
<a name="l65"><span class="ln">65   </span></a>        gc.gridx = </span><span class="s3">1</span><span class="s1">; gc.gridy = </span><span class="s3">1</span><span class="s1">;
<a name="l66"><span class="ln">66   </span></a>        gc.gridwidth = </span><span class="s3">1</span><span class="s1">; gc.fill = GridBagConstraints.BOTH;
<a name="l67"><span class="ln">67   </span></a>        gc.weightx = </span><span class="s3">1.0</span><span class="s1">; gc.weighty = </span><span class="s3">1.0</span><span class="s1">;
<a name="l68"><span class="ln">68   </span></a>        p.add(center, gc);
</span></pre>

<p>
    The GridBagLayout code suffers from a number of problems:

</p>

<ul>
    <li>It's verbose.</li>
    <li>It has the constraints mixed in with the bindings, making it difficult to read</li>
    <li>It uses explicit coordinate making it difficult to create and <em>very</em> difficult to come back and
        modify later.  It is possible to use relative constraints, but they still force you to account for
        other constraints in the layout.</li>
</ul>

<p>
    There a couple of libraries out there that alleviate some of GridBagLayout's problems through a wrapper API
    but they do not the fix the underlying fundamental issues.
</p>

<p>
    Here's the FormLayout code that creates the necessary layout:
</p>

<pre>
<span class="s1">
<a name="l28"><span class="ln">28   </span></a>        FormLayout layout = </span><span class="s0">new </span><span class="s1">FormLayout(
<a name="l29"><span class="ln">29   </span></a>                </span><span class="s2">&quot;f:d, f:d:g, f:d&quot;</span><span class="s1">,
<a name="l30"><span class="ln">30   </span></a>                </span><span class="s2">&quot;f:d, f:d:g, f:d&quot;</span><span class="s1">
<a name="l31"><span class="ln">31   </span></a>        );
<a name="l32"><span class="ln">32   </span></a>
<a name="l33"><span class="ln">33   </span></a>        JPanel p = </span><span class="s0">new </span><span class="s1">JPanel(layout);
<a name="l34"><span class="ln">34   </span></a>        CellConstraints cc = </span><span class="s0">new </span><span class="s1">CellConstraints();
<a name="l35"><span class="ln">35   </span></a>        p.add(north, cc.xyw(</span><span class="s3">1</span><span class="s1">, </span><span class="s3">1</span><span class="s1">, </span><span class="s3">3</span><span class="s1">));
<a name="l36"><span class="ln">36   </span></a>        p.add(south, cc.xyw(</span><span class="s3">1</span><span class="s1">, </span><span class="s3">3</span><span class="s1">, </span><span class="s3">3</span><span class="s1">));
<a name="l37"><span class="ln">37   </span></a>        p.add(east, cc.xy(</span><span class="s3">3</span><span class="s1">, </span><span class="s3">2</span><span class="s1">));
<a name="l38"><span class="ln">38   </span></a>        p.add(west, cc.xy(</span><span class="s3">1</span><span class="s1">, </span><span class="s3">2</span><span class="s1">));
<a name="l39"><span class="ln">39   </span></a>        p.add(center, cc.xy(</span><span class="s3">2</span><span class="s1">, </span><span class="s3">2</span><span class="s1">));
</span></pre>

<p>
    The FormLayout code improves on the GridBagLayout code.  It's not as verbose and cleaner.  However, it still
    uses explicit coordinates, making it difficult to create and modify.  The constraints are also split between the layout
    definition and the bindings, making it difficult for the programmer to mentally visualize the behavior of the
    panel components.  The programmer is also forced to break the layout down into rows and columns before creating
    the layout.  The constraints are also not particularly intuitive.
</p>

<p>
    Here's the ZoneLayout code that creates the necessary layout:
</p>

<pre>
<span class="s1">
<a name="l80"><span class="ln">80   </span></a>        ZoneLayout layout = ZoneLayoutFactory.newZoneLayout();
<a name="l81"><span class="ln">81   </span></a>        JPanel p = </span><span class="s0">new </span><span class="s1">JPanel(layout);
<a name="l82"><span class="ln">82   </span></a>        layout.addRow(</span><span class="s2">&quot;n..-~n&quot;</span><span class="s1">);
<a name="l83"><span class="ln">83   </span></a>        layout.addRow(</span><span class="s2">&quot;wc...e&quot;</span><span class="s1">);
<a name="l84"><span class="ln">84   </span></a>        layout.addRow(</span><span class="s2">&quot;|.*+.!&quot;</span><span class="s1">);
<a name="l85"><span class="ln">85   </span></a>        layout.addRow(</span><span class="s2">&quot;!....|&quot;</span><span class="s1">);
<a name="l86"><span class="ln">86   </span></a>        layout.addRow(</span><span class="s2">&quot;w...ce&quot;</span><span class="s1">);
<a name="l87"><span class="ln">87   </span></a>        layout.addRow(</span><span class="s2">&quot;s..-~s&quot;</span><span class="s1">);
<a name="l88"><span class="ln">88   </span></a>
<a name="l89"><span class="ln">89   </span></a>        p.add(north, </span><span class="s2">&quot;n&quot;</span><span class="s1">);
<a name="l90"><span class="ln">90   </span></a>        p.add(south, </span><span class="s2">&quot;s&quot;</span><span class="s1">);
<a name="l91"><span class="ln">91   </span></a>        p.add(east, </span><span class="s2">&quot;e&quot;</span><span class="s1">);
<a name="l92"><span class="ln">92   </span></a>        p.add(west, </span><span class="s2">&quot;w&quot;</span><span class="s1">);
<a name="l93"><span class="ln">93   </span></a>        p.add(center, </span><span class="s2">&quot;c&quot;</span><span class="s1">);
</span></pre>

<p>
    If this is your first time looking at ZoneLayout code, then this probably isn't particularly clear to you, but
    lines 82-87 tell you all you need to know about the layout.  The <a href="images/cheatsheet.png">cheatsheet</a> may
    come in handy here.
</p>

<ul>
    <li>The entire layout is contained within a few lines of code and is separate from the component binding.
        As a programmer, you only need to go to one place to change the layout.</li>
    <li>The layout does not use any explicit coordinates, everything is relative.  Moving components around is as simple
    as moving a few characters.</li>
    <li>It provides a pseudo visual picture of the resulting layout.  After working with ZoneLayout for a short period,
    you'll look at a layout like this and immediately recogize the 5 zones and how they are situated.</li>
</ul>

<p>
    It's important to note here how easy it would be to modify the ZoneLayout code.  If you wanted to add another
    button just below the north button, with ZoneLayout you would add 2 lines of code: the layout definition beneath
    the '<code>n</code>' Zone and the binding.  With the other 2 layout managers, you would have to spend significantly
    more time, including <em>updating all explicit coordinates affected by the addition.</em>
</p>

<h2>Comparing ZoneLayout with JGoodies' FormLayout</h2>

<p>
    In this more advanced comparison, we'll create a complex dialog with both ZoneLayout and FormLayout.
    Here is the dialog:
</p>

<div align="center">
    <img src="images/zoneLayoutComparison.png" alt="ZoneLayout Comparison Window"/>
</div>

<p>
    First, here is the relevant code using FormLayout (full listing
    <a href="com/atticlabs/zonelayout/swing/examples/FormLayoutComparison.java.html">here</a>).
</p>

<PRE>

<FONT style="font-family:monospaced;" COLOR="#000000">
<FONT COLOR=0 STYLE="font-style:normal">23   </FONT>    </FONT><FONT style="font-family:monospaced;" COLOR="#000080"><B>public</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000"> JPanel buildPanel() {
<FONT COLOR=0 STYLE="font-style:normal">24   </FONT>        FormLayout layout = </FONT><FONT style="font-family:monospaced;" COLOR="#000080"><B>new</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000"> FormLayout(
<FONT COLOR=0 STYLE="font-style:normal">25   </FONT>                </FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;d:g&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">,
<FONT COLOR=0 STYLE="font-style:normal">26   </FONT>                </FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;d, 3dlu, f:d:g, 3dlu, d&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">
<FONT COLOR=0 STYLE="font-style:normal">27   </FONT>
<FONT COLOR=0 STYLE="font-style:normal">28   </FONT>        );
<FONT COLOR=0 STYLE="font-style:normal">29   </FONT>
<FONT COLOR=0 STYLE="font-style:normal">30   </FONT>        FormLayout nameLayout = </FONT><FONT style="font-family:monospaced;" COLOR="#000080"><B>new</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000"> FormLayout(
<FONT COLOR=0 STYLE="font-style:normal">31   </FONT>                </FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;r:d, 3dlu, d:g, 7dlu, r:d, 3dlu, d:g&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">,
<FONT COLOR=0 STYLE="font-style:normal">32   </FONT>                </FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;d, 3dlu, d, 3dlu, d&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">
<FONT COLOR=0 STYLE="font-style:normal">33   </FONT>
<FONT COLOR=0 STYLE="font-style:normal">34   </FONT>        );
<FONT COLOR=0 STYLE="font-style:normal">35   </FONT>
<FONT COLOR=0 STYLE="font-style:normal">36   </FONT>        FormLayout emailLayout = </FONT><FONT style="font-family:monospaced;" COLOR="#000080"><B>new</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000"> FormLayout(
<FONT COLOR=0 STYLE="font-style:normal">37   </FONT>                </FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;r:d, 3dlu, d:g, 3dlu, d&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">,
<FONT COLOR=0 STYLE="font-style:normal">38   </FONT>                </FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;d, 3dlu, d, 3dlu, d, 3dlu, d, f:d:g&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">
<FONT COLOR=0 STYLE="font-style:normal">39   </FONT>
<FONT COLOR=0 STYLE="font-style:normal">40   </FONT>        );
<FONT COLOR=0 STYLE="font-style:normal">41   </FONT>
<FONT COLOR=0 STYLE="font-style:normal">42   </FONT>        PanelBuilder builder = </FONT><FONT style="font-family:monospaced;" COLOR="#000080"><B>new</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000"> PanelBuilder(nameLayout);
<FONT COLOR=0 STYLE="font-style:normal">43   </FONT>        CellConstraints cc = </FONT><FONT style="font-family:monospaced;" COLOR="#000080"><B>new</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000"> CellConstraints();
<FONT COLOR=0 STYLE="font-style:normal">44   </FONT>        builder.addLabel(</FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;First Name:&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">, cc.xy(</FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">1</FONT><FONT style="font-family:monospaced;" COLOR="#000000">, </FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">1</FONT><FONT style="font-family:monospaced;" COLOR="#000000">));
<FONT COLOR=0 STYLE="font-style:normal">45   </FONT>        builder.add(</FONT><FONT style="font-family:monospaced;" COLOR="#000080"><B>new</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000"> JTextField(</FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">15</FONT><FONT style="font-family:monospaced;" COLOR="#000000">), cc.xy(</FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">3</FONT><FONT style="font-family:monospaced;" COLOR="#000000">, </FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">1</FONT><FONT style="font-family:monospaced;" COLOR="#000000">));
<FONT COLOR=0 STYLE="font-style:normal">46   </FONT>        builder.addLabel(</FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;Last Name:&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">, cc.xy(</FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">5</FONT><FONT style="font-family:monospaced;" COLOR="#000000">, </FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">1</FONT><FONT style="font-family:monospaced;" COLOR="#000000">));
<FONT COLOR=0 STYLE="font-style:normal">47   </FONT>        builder.add(</FONT><FONT style="font-family:monospaced;" COLOR="#000080"><B>new</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000"> JTextField(</FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">15</FONT><FONT style="font-family:monospaced;" COLOR="#000000">), cc.xy(</FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">7</FONT><FONT style="font-family:monospaced;" COLOR="#000000">, </FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">1</FONT><FONT style="font-family:monospaced;" COLOR="#000000">));
<FONT COLOR=0 STYLE="font-style:normal">48   </FONT>        builder.addLabel(</FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;Title:&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">, cc.xy(</FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">1</FONT><FONT style="font-family:monospaced;" COLOR="#000000">, </FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">3</FONT><FONT style="font-family:monospaced;" COLOR="#000000">));
<FONT COLOR=0 STYLE="font-style:normal">49   </FONT>        builder.add(</FONT><FONT style="font-family:monospaced;" COLOR="#000080"><B>new</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000"> JTextField(</FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">15</FONT><FONT style="font-family:monospaced;" COLOR="#000000">), cc.xy(</FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">3</FONT><FONT style="font-family:monospaced;" COLOR="#000000">, </FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">3</FONT><FONT style="font-family:monospaced;" COLOR="#000000">));
<FONT COLOR=0 STYLE="font-style:normal">50   </FONT>        builder.addLabel(</FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;Nickname:&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">, cc.xy(</FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">5</FONT><FONT style="font-family:monospaced;" COLOR="#000000">, </FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">3</FONT><FONT style="font-family:monospaced;" COLOR="#000000">));
<FONT COLOR=0 STYLE="font-style:normal">51   </FONT>        builder.add(</FONT><FONT style="font-family:monospaced;" COLOR="#000080"><B>new</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000"> JTextField(</FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">15</FONT><FONT style="font-family:monospaced;" COLOR="#000000">), cc.xy(</FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">7</FONT><FONT style="font-family:monospaced;" COLOR="#000000">, </FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">3</FONT><FONT style="font-family:monospaced;" COLOR="#000000">));
<FONT COLOR=0 STYLE="font-style:normal">52   </FONT>        builder.addLabel(</FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;Display Format:&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">, cc.xy(</FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">1</FONT><FONT style="font-family:monospaced;" COLOR="#000000">, </FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">5</FONT><FONT style="font-family:monospaced;" COLOR="#000000">));
<FONT COLOR=0 STYLE="font-style:normal">53   </FONT>        builder.add(</FONT><FONT style="font-family:monospaced;" COLOR="#000080"><B>new</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000"> JComboBox(values), cc.xyw(</FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">3</FONT><FONT style="font-family:monospaced;" COLOR="#000000">, </FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">5</FONT><FONT style="font-family:monospaced;" COLOR="#000000">, </FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">5</FONT><FONT style="font-family:monospaced;" COLOR="#000000">));
<FONT COLOR=0 STYLE="font-style:normal">54   </FONT>        JPanel namePanel = builder.getPanel();
<FONT COLOR=0 STYLE="font-style:normal">55   </FONT>        namePanel.setBorder(BorderFactory.createCompoundBorder(
<FONT COLOR=0 STYLE="font-style:normal">56   </FONT>                BorderFactory.createTitledBorder(</FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;Name&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">),
<FONT COLOR=0 STYLE="font-style:normal">57   </FONT>                BorderFactory.createEmptyBorder(</FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">5</FONT><FONT style="font-family:monospaced;" COLOR="#000000">, </FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">5</FONT><FONT style="font-family:monospaced;" COLOR="#000000">, </FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">5</FONT><FONT style="font-family:monospaced;" COLOR="#000000">, </FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">5</FONT><FONT style="font-family:monospaced;" COLOR="#000000">)));
<FONT COLOR=0 STYLE="font-style:normal">58   </FONT>
<FONT COLOR=0 STYLE="font-style:normal">59   </FONT>        builder = </FONT><FONT style="font-family:monospaced;" COLOR="#000080"><B>new</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000"> PanelBuilder(emailLayout);
<FONT COLOR=0 STYLE="font-style:normal">60   </FONT>        cc = </FONT><FONT style="font-family:monospaced;" COLOR="#000080"><B>new</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000"> CellConstraints();
<FONT COLOR=0 STYLE="font-style:normal">61   </FONT>        builder.addLabel(</FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;Email Address:&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">, cc.xy(</FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">1</FONT><FONT style="font-family:monospaced;" COLOR="#000000">, </FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">1</FONT><FONT style="font-family:monospaced;" COLOR="#000000">));
<FONT COLOR=0 STYLE="font-style:normal">62   </FONT>        builder.add(</FONT><FONT style="font-family:monospaced;" COLOR="#000080"><B>new</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000"> JTextField(</FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">15</FONT><FONT style="font-family:monospaced;" COLOR="#000000">), cc.xy(</FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">3</FONT><FONT style="font-family:monospaced;" COLOR="#000000">, </FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">1</FONT><FONT style="font-family:monospaced;" COLOR="#000000">));
<FONT COLOR=0 STYLE="font-style:normal">63   </FONT>        builder.add(</FONT><FONT style="font-family:monospaced;" COLOR="#000080"><B>new</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000"> JButton(</FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;Add&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">), cc.xy(</FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">5</FONT><FONT style="font-family:monospaced;" COLOR="#000000">, </FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">1</FONT><FONT style="font-family:monospaced;" COLOR="#000000">));
<FONT COLOR=0 STYLE="font-style:normal">64   </FONT>
<FONT COLOR=0 STYLE="font-style:normal">65   </FONT>        builder.add(</FONT><FONT style="font-family:monospaced;" COLOR="#000080"><B>new</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000"> JList(values), cc.xywh(</FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">1</FONT><FONT style="font-family:monospaced;" COLOR="#000000">, </FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">3</FONT><FONT style="font-family:monospaced;" COLOR="#000000">, </FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">3</FONT><FONT style="font-family:monospaced;" COLOR="#000000">, </FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">6</FONT><FONT style="font-family:monospaced;" COLOR="#000000">));
<FONT COLOR=0 STYLE="font-style:normal">66   </FONT>
<FONT COLOR=0 STYLE="font-style:normal">67   </FONT>        builder.add(</FONT><FONT style="font-family:monospaced;" COLOR="#000080"><B>new</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000"> JButton(</FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;Edit&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">), cc.xy(</FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">5</FONT><FONT style="font-family:monospaced;" COLOR="#000000">, </FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">3</FONT><FONT style="font-family:monospaced;" COLOR="#000000">));
<FONT COLOR=0 STYLE="font-style:normal">68   </FONT>        builder.add(</FONT><FONT style="font-family:monospaced;" COLOR="#000080"><B>new</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000"> JButton(</FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;Remove&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">), cc.xy(</FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">5</FONT><FONT style="font-family:monospaced;" COLOR="#000000">, </FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">5</FONT><FONT style="font-family:monospaced;" COLOR="#000000">));
<FONT COLOR=0 STYLE="font-style:normal">69   </FONT>        builder.add(</FONT><FONT style="font-family:monospaced;" COLOR="#000080"><B>new</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000"> JButton(</FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;Advanced&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">), cc.xy(</FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">5</FONT><FONT style="font-family:monospaced;" COLOR="#000000">, </FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">7</FONT><FONT style="font-family:monospaced;" COLOR="#000000">));
<FONT COLOR=0 STYLE="font-style:normal">70   </FONT>
<FONT COLOR=0 STYLE="font-style:normal">71   </FONT>        JPanel emailPanel = builder.getPanel();
<FONT COLOR=0 STYLE="font-style:normal">72   </FONT>        emailPanel.setBorder(BorderFactory.createCompoundBorder(
<FONT COLOR=0 STYLE="font-style:normal">73   </FONT>                BorderFactory.createTitledBorder(</FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;E-mail&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">),
<FONT COLOR=0 STYLE="font-style:normal">74   </FONT>                BorderFactory.createEmptyBorder(</FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">5</FONT><FONT style="font-family:monospaced;" COLOR="#000000">, </FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">5</FONT><FONT style="font-family:monospaced;" COLOR="#000000">, </FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">5</FONT><FONT style="font-family:monospaced;" COLOR="#000000">, </FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">5</FONT><FONT style="font-family:monospaced;" COLOR="#000000">)));
<FONT COLOR=0 STYLE="font-style:normal">75   </FONT>
<FONT COLOR=0 STYLE="font-style:normal">76   </FONT>        builder = </FONT><FONT style="font-family:monospaced;" COLOR="#000080"><B>new</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000"> PanelBuilder(layout);
<FONT COLOR=0 STYLE="font-style:normal">77   </FONT>        builder.add(namePanel, cc.xy(</FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">1</FONT><FONT style="font-family:monospaced;" COLOR="#000000">, </FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">1</FONT><FONT style="font-family:monospaced;" COLOR="#000000">));
<FONT COLOR=0 STYLE="font-style:normal">78   </FONT>        builder.add(emailPanel, cc.xy(</FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">1</FONT><FONT style="font-family:monospaced;" COLOR="#000000">, </FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">3</FONT><FONT style="font-family:monospaced;" COLOR="#000000">));
<FONT COLOR=0 STYLE="font-style:normal">79   </FONT>        builder.add(ButtonBarFactory.buildOKCancelBar(
<FONT COLOR=0 STYLE="font-style:normal">80   </FONT>                </FONT><FONT style="font-family:monospaced;" COLOR="#000080"><B>new</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000"> JButton(</FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;OK&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">), </FONT><FONT style="font-family:monospaced;" COLOR="#000080"><B>new</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000"> JButton(</FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;Cancel&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">)),
<FONT COLOR=0 STYLE="font-style:normal">81   </FONT>                cc.xywh(</FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">1</FONT><FONT style="font-family:monospaced;" COLOR="#000000">, </FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">5</FONT><FONT style="font-family:monospaced;" COLOR="#000000">, </FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">1</FONT><FONT style="font-family:monospaced;" COLOR="#000000">, </FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">1</FONT><FONT style="font-family:monospaced;" COLOR="#000000">, </FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;right, bottom&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">));
<FONT COLOR=0 STYLE="font-style:normal">82   </FONT>        JPanel p = builder.getPanel();
<FONT COLOR=0 STYLE="font-style:normal">83   </FONT>        p.setBorder(BorderFactory.createEmptyBorder(</FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">5</FONT><FONT style="font-family:monospaced;" COLOR="#000000">, </FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">5</FONT><FONT style="font-family:monospaced;" COLOR="#000000">, </FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">5</FONT><FONT style="font-family:monospaced;" COLOR="#000000">, </FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">5</FONT><FONT style="font-family:monospaced;" COLOR="#000000">));
<FONT COLOR=0 STYLE="font-style:normal">84   </FONT>
<FONT COLOR=0 STYLE="font-style:normal">85   </FONT>        </FONT><FONT style="font-family:monospaced;" COLOR="#000080"><B>return</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000"> p;
<FONT COLOR=0 STYLE="font-style:normal">86   </FONT>    }
</FONT></PRE>

<p>
    Imagine this code was handed off to you by your predecessor and now the customer wants you to add a button along
    the right side of the panel.  What steps would you take?  You'd probably:
</p>

<ol>
    <li>Run the code so you can get a mental picture of the layout</li>
    <li>Find the components in the source that are near where the new button needs to go</li>
    <li>Find the coordinates of those near-by components</li>
    <li>Figure out the row and/or column definitions for the near-by components</li>
    <li>Figure out what row(s) and/or column(s) the new button needs to go in</li>
    <li>Spend some time figuring out how adding rows or columns affects <b>every other component in the layout</b></li>
    <li>Add the new row and/or column definitions</li>
    <li>Add the new button to the panel with it's coordinate</li>
    <li><b>Verify and update the coordinates of every other component in the layout</b></li>
    <li>Run through the test/fix cycle a few times since the explicit coordinate system is accident prone</li>
</ol>

<p>
    Now let's look at the functionally equivalent code using ZoneLayout (full listing
    <a href="com/atticlabs/zonelayout/swing/examples/ZoneLayoutComparison.java.html">here</a>):
</p>

<PRE>
<FONT style="font-family:monospaced;" COLOR="#000000">
<FONT COLOR=0 STYLE="font-style:normal">23   </FONT>    </FONT><FONT style="font-family:monospaced;" COLOR="#000080"><B>public</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000"> JPanel buildPanel() {
<FONT COLOR=0 STYLE="font-style:normal">24   </FONT>        ZoneLayout layout = ZoneLayoutFactory.newZoneLayout();
<FONT COLOR=0 STYLE="font-style:normal">25   </FONT>        layout.addRow(</FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;a-~a&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">);
<FONT COLOR=0 STYLE="font-style:normal">26   </FONT>        layout.addRow(</FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;b+*b&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">);
<FONT COLOR=0 STYLE="font-style:normal">27   </FONT>        layout.addRow(</FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;6...&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">);
<FONT COLOR=0 STYLE="font-style:normal">28   </FONT>        layout.addRow(</FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;c&gt;.c&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">);
<FONT COLOR=0 STYLE="font-style:normal">29   </FONT>
<FONT COLOR=0 STYLE="font-style:normal">30   </FONT>        ZoneLayout aLayout = ZoneLayoutFactory.newZoneLayout();
<FONT COLOR=0 STYLE="font-style:normal">31   </FONT>        aLayout.addRow(</FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;a&gt;a2b-~b3c&gt;c2d-~d&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">, </FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;valueRow&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">);
<FONT COLOR=0 STYLE="font-style:normal">32   </FONT>        aLayout.addRow(</FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;6................&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">, </FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;valueRow&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">);
<FONT COLOR=0 STYLE="font-style:normal">33   </FONT>        aLayout.addRow(</FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;e&gt;e2f......-~...f&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">);
<FONT COLOR=0 STYLE="font-style:normal">34   </FONT>
<FONT COLOR=0 STYLE="font-style:normal">35   </FONT>        ZoneLayout bLayout = ZoneLayoutFactory.newZoneLayout();
<FONT COLOR=0 STYLE="font-style:normal">36   </FONT>        bLayout.addRow(</FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;a&gt;a2b-~b2c-c&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">);
<FONT COLOR=0 STYLE="font-style:normal">37   </FONT>        bLayout.addRow(</FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;...........6&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">);
<FONT COLOR=0 STYLE="font-style:normal">38   </FONT>        bLayout.addRow(</FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;g........d-d&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">);
<FONT COLOR=0 STYLE="font-style:normal">39   </FONT>        bLayout.addRow(</FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;...........6&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">);
<FONT COLOR=0 STYLE="font-style:normal">40   </FONT>        bLayout.addRow(</FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;...+*....e-e&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">);
<FONT COLOR=0 STYLE="font-style:normal">41   </FONT>        bLayout.addRow(</FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;...........6&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">);
<FONT COLOR=0 STYLE="font-style:normal">42   </FONT>        bLayout.addRow(</FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;.........f-f&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">);
<FONT COLOR=0 STYLE="font-style:normal">43   </FONT>        bLayout.addRow(</FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;.......g...!&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">);
<FONT COLOR=0 STYLE="font-style:normal">44   </FONT>
<FONT COLOR=0 STYLE="font-style:normal">45   </FONT>        JPanel namePanel = </FONT><FONT style="font-family:monospaced;" COLOR="#000080"><B>new</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000"> JPanel(aLayout);
<FONT COLOR=0 STYLE="font-style:normal">46   </FONT>        aLayout.insertTemplate(</FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;valueRow&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">);
<FONT COLOR=0 STYLE="font-style:normal">47   </FONT>        namePanel.add(</FONT><FONT style="font-family:monospaced;" COLOR="#000080"><B>new</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000"> JLabel(</FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;First Name:&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">), </FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;a&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">);
<FONT COLOR=0 STYLE="font-style:normal">48   </FONT>        namePanel.add(</FONT><FONT style="font-family:monospaced;" COLOR="#000080"><B>new</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000"> JTextField(</FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">15</FONT><FONT style="font-family:monospaced;" COLOR="#000000">), </FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;b&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">);
<FONT COLOR=0 STYLE="font-style:normal">49   </FONT>        namePanel.add(</FONT><FONT style="font-family:monospaced;" COLOR="#000080"><B>new</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000"> JLabel(</FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;Last Name:&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">), </FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;c&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">);
<FONT COLOR=0 STYLE="font-style:normal">50   </FONT>        namePanel.add(</FONT><FONT style="font-family:monospaced;" COLOR="#000080"><B>new</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000"> JTextField(</FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">15</FONT><FONT style="font-family:monospaced;" COLOR="#000000">), </FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;d&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">);
<FONT COLOR=0 STYLE="font-style:normal">51   </FONT>        System.out.println(</FONT><FONT style="font-family:monospaced;" COLOR="#000080"><B>new</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000"> JTextField(</FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">15</FONT><FONT style="font-family:monospaced;" COLOR="#000000">).getMinimumSize());
<FONT COLOR=0 STYLE="font-style:normal">52   </FONT>        aLayout.insertTemplate(</FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;valueRow&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">);
<FONT COLOR=0 STYLE="font-style:normal">53   </FONT>        namePanel.add(</FONT><FONT style="font-family:monospaced;" COLOR="#000080"><B>new</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000"> JLabel(</FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;Title:&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">), </FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;a&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">);
<FONT COLOR=0 STYLE="font-style:normal">54   </FONT>        namePanel.add(</FONT><FONT style="font-family:monospaced;" COLOR="#000080"><B>new</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000"> JTextField(</FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">15</FONT><FONT style="font-family:monospaced;" COLOR="#000000">), </FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;b&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">);
<FONT COLOR=0 STYLE="font-style:normal">55   </FONT>        namePanel.add(</FONT><FONT style="font-family:monospaced;" COLOR="#000080"><B>new</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000"> JLabel(</FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;Nickname:&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">), </FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;c&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">);
<FONT COLOR=0 STYLE="font-style:normal">56   </FONT>        namePanel.add(</FONT><FONT style="font-family:monospaced;" COLOR="#000080"><B>new</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000"> JTextField(</FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">15</FONT><FONT style="font-family:monospaced;" COLOR="#000000">), </FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;d&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">);
<FONT COLOR=0 STYLE="font-style:normal">57   </FONT>        namePanel.add(</FONT><FONT style="font-family:monospaced;" COLOR="#000080"><B>new</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000"> JLabel(</FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;Display Format:&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">), </FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;e&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">);
<FONT COLOR=0 STYLE="font-style:normal">58   </FONT>        namePanel.add(</FONT><FONT style="font-family:monospaced;" COLOR="#000080"><B>new</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000"> JComboBox(values), </FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;f&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">);
<FONT COLOR=0 STYLE="font-style:normal">59   </FONT>        namePanel.setBorder(BorderFactory.createCompoundBorder(
<FONT COLOR=0 STYLE="font-style:normal">60   </FONT>                BorderFactory.createTitledBorder(</FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;Name&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">),
<FONT COLOR=0 STYLE="font-style:normal">61   </FONT>                BorderFactory.createEmptyBorder(</FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">5</FONT><FONT style="font-family:monospaced;" COLOR="#000000">, </FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">5</FONT><FONT style="font-family:monospaced;" COLOR="#000000">, </FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">5</FONT><FONT style="font-family:monospaced;" COLOR="#000000">, </FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">5</FONT><FONT style="font-family:monospaced;" COLOR="#000000">)));
<FONT COLOR=0 STYLE="font-style:normal">62   </FONT>
<FONT COLOR=0 STYLE="font-style:normal">63   </FONT>        JPanel emailPanel = </FONT><FONT style="font-family:monospaced;" COLOR="#000080"><B>new</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000"> JPanel(bLayout);
<FONT COLOR=0 STYLE="font-style:normal">64   </FONT>        emailPanel.add(</FONT><FONT style="font-family:monospaced;" COLOR="#000080"><B>new</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000"> JLabel(</FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;Email Address:&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">), </FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;a&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">);
<FONT COLOR=0 STYLE="font-style:normal">65   </FONT>        emailPanel.add(</FONT><FONT style="font-family:monospaced;" COLOR="#000080"><B>new</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000"> JTextField(</FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">15</FONT><FONT style="font-family:monospaced;" COLOR="#000000">), </FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;b&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">);
<FONT COLOR=0 STYLE="font-style:normal">66   </FONT>        emailPanel.add(</FONT><FONT style="font-family:monospaced;" COLOR="#000080"><B>new</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000"> JButton(</FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;Add&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">), </FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;c&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">);
<FONT COLOR=0 STYLE="font-style:normal">67   </FONT>        emailPanel.add(</FONT><FONT style="font-family:monospaced;" COLOR="#000080"><B>new</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000"> JButton(</FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;Edit&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">), </FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;d&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">);
<FONT COLOR=0 STYLE="font-style:normal">68   </FONT>        emailPanel.add(</FONT><FONT style="font-family:monospaced;" COLOR="#000080"><B>new</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000"> JButton(</FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;Remove&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">), </FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;e&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">);
<FONT COLOR=0 STYLE="font-style:normal">69   </FONT>        emailPanel.add(</FONT><FONT style="font-family:monospaced;" COLOR="#000080"><B>new</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000"> JButton(</FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;Advanced&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">), </FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;f&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">);
<FONT COLOR=0 STYLE="font-style:normal">70   </FONT>        emailPanel.add(</FONT><FONT style="font-family:monospaced;" COLOR="#000080"><B>new</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000"> JList(values), </FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;g&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">);
<FONT COLOR=0 STYLE="font-style:normal">71   </FONT>
<FONT COLOR=0 STYLE="font-style:normal">72   </FONT>        emailPanel.setBorder(BorderFactory.createCompoundBorder(
<FONT COLOR=0 STYLE="font-style:normal">73   </FONT>                BorderFactory.createTitledBorder(</FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;E-mail&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">),
<FONT COLOR=0 STYLE="font-style:normal">74   </FONT>                BorderFactory.createEmptyBorder(</FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">5</FONT><FONT style="font-family:monospaced;" COLOR="#000000">, </FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">5</FONT><FONT style="font-family:monospaced;" COLOR="#000000">, </FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">5</FONT><FONT style="font-family:monospaced;" COLOR="#000000">, </FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">5</FONT><FONT style="font-family:monospaced;" COLOR="#000000">)));
<FONT COLOR=0 STYLE="font-style:normal">75   </FONT>
<FONT COLOR=0 STYLE="font-style:normal">76   </FONT>
<FONT COLOR=0 STYLE="font-style:normal">77   </FONT>        JPanel p = </FONT><FONT style="font-family:monospaced;" COLOR="#000080"><B>new</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000"> JPanel(layout);
<FONT COLOR=0 STYLE="font-style:normal">78   </FONT>
<FONT COLOR=0 STYLE="font-style:normal">79   </FONT>        p.add(namePanel, </FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;a&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">);
<FONT COLOR=0 STYLE="font-style:normal">80   </FONT>        p.add(emailPanel, </FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;b&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">);
<FONT COLOR=0 STYLE="font-style:normal">81   </FONT>        p.add(ButtonBarFactory.buildOKCancelBar(
<FONT COLOR=0 STYLE="font-style:normal">82   </FONT>                </FONT><FONT style="font-family:monospaced;" COLOR="#000080"><B>new</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000"> JButton(</FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;OK&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">),
<FONT COLOR=0 STYLE="font-style:normal">83   </FONT>                </FONT><FONT style="font-family:monospaced;" COLOR="#000080"><B>new</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000"> JButton(</FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;Cancel&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">)), </FONT><FONT style="font-family:monospaced;" COLOR="#008000"><B>&quot;c&quot;</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000">);
<FONT COLOR=0 STYLE="font-style:normal">84   </FONT>
<FONT COLOR=0 STYLE="font-style:normal">85   </FONT>        p.setBorder(BorderFactory.createEmptyBorder(</FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">5</FONT><FONT style="font-family:monospaced;" COLOR="#000000">, </FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">5</FONT><FONT style="font-family:monospaced;" COLOR="#000000">, </FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">5</FONT><FONT style="font-family:monospaced;" COLOR="#000000">, </FONT><FONT style="font-family:monospaced;" COLOR="#0000ff">5</FONT><FONT style="font-family:monospaced;" COLOR="#000000">));
<FONT COLOR=0 STYLE="font-style:normal">86   </FONT>        </FONT><FONT style="font-family:monospaced;" COLOR="#000080"><B>return</B></FONT><FONT style="font-family:monospaced;" COLOR="#000000"> p;
<FONT COLOR=0 STYLE="font-style:normal">87   </FONT>    }</FONT>
</PRE>

<p>
    If this is your first time looking at ZoneLayout code, you're probably wondering what the gibberish is
    between lines 25 - 43.  That's the 2-dimensional regular-expression-like language that
    ZoneLayout uses to define layouts.  Each pair of letters defines the upper-left and lower-right corners of
    a zone and any control characters within those boundaries modify that zone (periods are for spacing only and are
    ignored).  Digits are preset spacer zones.  If you're wondering what the control characters do, check out
    the <a href="images/cheatsheet.png">cheatsheet</a> or the <a href="manual.php#modifiers">modifiers<a> in
    the <a href="manual.php">manual</a>. When adding components, the constraint is just the letter of the zone
    to fill with the component.
</p>

<p>
    The line count is essentially the same, but with ZoneLayout on lines 25 - 43 you effectively get a pseudo-visual
    picture of what the layout looks like:
</p>

<div align="center">
    <img src="images/comparisonLayout.png" alt="Comparison ZoneLayout"/>
</div>

<p>
    This picture allows you to quickly grasp the layout of the panel.  Here's the final result for the ZoneLayout
    implementation with the zone areas highlighted so you can see the resemblance:
</p>

<div align="center">
    <img src="images/zoneLayoutComparisonOverlay.png" alt="ZoneLayout Comparison Window"/>
</div>

<p>
    If you're really paying attention, you probably noticed a little bit of trickery in the
    ZoneLayout code.  On lines 31 - 32 we use a <a href="manual.php#templates">template</a> so that we don't
    have to repeat ourselves for the "First Name/Last Name" and "Title/Nickname" component layouts. We just
    insert the template twice when adding the components.
</p>

<p>
    A picture's worth a thousand words, right?  After working with ZoneLayout for a short period, you'll be able to
    look at a layout and immediately deduce what's going on and what goes where.  When a customer
    wants you to add a button, you would just:
</p>

<ol>
    <li>Find the components that are nearby where the new button needs to go</li>
    <li>Find the zones with those components in the layout</li>
    <li>Add a row or two to contain the new zone for the button</li>
    <li>Add the button to the panel and bind it to the new zone</li>
    <li>Run through the test/fix cycle once or twice</li>
</ol>

<p>
    These steps can be accomplished very quickly in most layouts, significantly reducing the time it
    takes to refactor layouts when compared to FormLayout or other traditional layout managers.
</p>

<p>
    Here are the final results for the FormLayout and ZoneLayout implementations respectively (more below):
</p>

<div align="center">
    <img src="images/formLayoutComparison.png" alt="ZoneLayout Comparison Window"/>
</div>

<div align="center">
    <img src="images/zoneLayoutComparison.png" alt="ZoneLayout Comparison Window"/>
</div>

<p>
    The focus so far has been on refactoring an existing layout, but ZoneLayout shines when creating
    layouts as well.  Traditional layout managers are so hard to use that many recommend that you start
    out on paper first, break everything down into columns and rows, and then convert the layout into code.
    They also attempt to compensate for their deficiencies by using panel builders but in the end you still
    have code that is very difficult to read. ZoneLayout is simple enough that you can go directly
    to code and quickly build up advanced layouts since you have a mental picture to work with as you go.
</p>

<p>
    Many suggest using visual GUI builders to compensate for the difficulty in using traditional layout managers,
    but they have problems as well:
<ul>
    <li>What you see is not exactly what you get, so you still have to run the code to see the end result.</li>
    <li>Finding, dragging, dropping, aligning, and setting parameters on controls is a slow process in a GUI.</li>
    <li>They either rely on proprietary binary formats or generate and parse code you cannot touch</li>
    <li>If they generate code, they're modifying your source files</li>
    <li>If a visual builder relies on a 3rd party layout manager, you need to make sure the versions are in synch</li>
    <li>They force you to context switch out of your source code</li>
    <li>They force everyone on your team to use the same editor when doing layouts</li>
    <li>They do not handle dynamic forms where the number of controls is dependent on the data</li>
</ul>

</p>

<?php
}

if (! $file_served) {
    include_once('includes/page.php');
}

?>