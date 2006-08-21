/* 
 * -------------------------------------------------------------------
 * This source code, its documentation and all appendant files
 * are protected by copyright law. All rights reserved.
 *
 * Copyright, 2003 - 2006
 * University of Konstanz, Germany.
 * Chair for Bioinformatics and Information Mining
 * Prof. Dr. Michael R. Berthold
 *
 * You may not modify, publish, transmit, transfer or sell, reproduce,
 * create derivative works from, distribute, perform, display, or in
 * any way exploit any of the content, in whole or in part, except as
 * otherwise expressly permitted in writing by the copyright owner or
 * as specified in the license file distributed with this product.
 *
 * If you have any questions please contact the copyright holder:
 * website: www.knime.org
 * email: contact@knime.org
 * -------------------------------------------------------------------
 * 
 * History
 *   30.10.2005 (mb): created
 */
package org.knime.base.node.io.predictor;

import org.knime.core.node.defaultnodedialog.DefaultNodeDialogPane;
import org.knime.core.node.defaultnodedialog.DialogComponentFileChooser;

/** Dialog for the ModelContent Reader Node - allows user to choose file name
 * and directory.
 * 
 * @author M. Berthold, University of Konstanz
 */
public class PredictorReaderNodeDialog extends DefaultNodeDialogPane {
    /** Constructor: create NodeDialog with just one default component,
     * the file chooser entry.
     */
    public PredictorReaderNodeDialog() {
        super();
        DialogComponentFileChooser fcComp
            = new DialogComponentFileChooser(PredictorReaderNodeModel.FILENAME,
                    PredictorReaderNodeDialog.class.getName(),
                new String[]{".pmml.gz", ".pmml"});
        this.addDialogComponent(fcComp);
    }
    
}
