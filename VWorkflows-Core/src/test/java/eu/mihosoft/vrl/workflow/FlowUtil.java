/*
 * Copyright 2012-2021 Michael Hoffer <info@michaelhoffer.de>. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * Please cite the following publication(s):
 *
 * M. Hoffer, C.Poliwoda, G.Wittum. Visual Reflection Library -
 * A Framework for Declarative GUI Programming on the Java Platform.
 * Computing and Visualization in Science, 2011, in press.
 *
 * THIS SOFTWARE IS PROVIDED BY Michael Hoffer <info@michaelhoffer.de> "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL Michael Hoffer <info@michaelhoffer.de> OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and should not be interpreted as representing official policies, either expressed
 * or implied, of Michael Hoffer <info@michaelhoffer.de>.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.mihosoft.vrl.workflow;


import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Michael Hoffer <info@michaelhoffer.de>
 */
public class FlowUtil {

    /**
     * Creates a flow with specified width and depth.
     *
     * @param workflow parent workflow
     * @param depth flow depth (number of nested nodes)
     * @param width flow width (number of nodes per layer)
     */
    public static void createFlow(VFlow workflow, int depth, int width) {

        // stop if we reached deppest layer
        if (depth < 1) {
            return;
        }

        String[] connectionTypes = {"control", "data", "event"};

        List<VNode> prevNodes = new ArrayList<>();

        // create nodes in current layer
        for (int i = 0; i < width; i++) {

            VNode n;

            // every second node shall be a subflow
            if (i % 2 == 0) {
                // create subflow
                VFlow subFlow = workflow.newSubFlow();
                n = subFlow.getModel();
                createFlow(subFlow, depth - 1, width);
            } else {
                //create leaf node
                n = workflow.newNode();
            }

            n.setTitle("Node " + i);

            // every third node shall have the same connection type
            // colors for "control", "data" and "event" are currently hardcoded
            // in skin. This will change!
            String type = connectionTypes[i % connectionTypes.length];

            n.addInput(type);
            n.addOutput(type);

            // specify node size
            n.setWidth(300);
            n.setHeight(200);

            // gap between nodes
            int gap = 30;

            int numNodesPerRow = 3;

            // specify node position (we use grid layout)
            n.setX(gap + (i % numNodesPerRow) * (n.getWidth() + gap));
            n.setY(gap + (i / numNodesPerRow) * (n.getHeight() + gap));

            if (i >= connectionTypes.length) {

                workflow.connect(prevNodes.get(0).getOutputs().get(0),
                        n.getInputs().get(0));

                prevNodes.remove(0);
            }

            prevNodes.add(n);
        }
    }
}
