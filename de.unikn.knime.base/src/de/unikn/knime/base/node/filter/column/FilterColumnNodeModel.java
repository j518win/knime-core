/* --------------------------------------------------------------------- *
 *   This source code, its documentation and all appendant files         *
 *   are protected by copyright law. All rights reserved.                *
 *                                                                       *
 *   Copyright, 2003 - 2006                                              *
 *   Universitaet Konstanz, Germany.                                     *
 *   Lehrstuhl fuer Angewandte Informatik                                *
 *   Prof. Dr. Michael R. Berthold                                       *
 *                                                                       *
 *   You may not modify, publish, transmit, transfer or sell, reproduce, *
 *   create derivative works from, distribute, perform, display, or in   *
 *   any way exploit any of the content, in whole or in part, except as  *
 *   otherwise expressly permitted in writing by the copyright owner.    *
 * --------------------------------------------------------------------- *
 */
package de.unikn.knime.base.node.filter.column;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import de.unikn.knime.core.data.DataTableSpec;
import de.unikn.knime.core.data.container.ColumnRearranger;
import de.unikn.knime.core.node.BufferedDataTable;
import de.unikn.knime.core.node.CanceledExecutionException;
import de.unikn.knime.core.node.ExecutionContext;
import de.unikn.knime.core.node.ExecutionMonitor;
import de.unikn.knime.core.node.InvalidSettingsException;
import de.unikn.knime.core.node.NodeModel;
import de.unikn.knime.core.node.NodeSettingsRO;
import de.unikn.knime.core.node.NodeSettingsWO;

/**
 * The model for the column filter which extracts certain columns from the input
 * <code>DataTable</code> using a list of columns to exclude.
 * 
 * @author Christoph Sieb, University of Konstanz
 * @author Thomas Gabriel, University of Konstanz
 */
final class FilterColumnNodeModel extends NodeModel {

    /**
     * The input port used here.
     */
    static final int INPORT = 0;

    /**
     * The output port used here.
     */
    static final int OUTPORT = 0;

    /**
     * the excluded settings.
     */
    static final String KEY = "exclude";

    /*
     * List contains the data cells to exclude.
     */
    private final ArrayList<String> m_list;

    /**
     * Creates a new filter model with one and in- and output.
     * 
     * @see #reset
     */
    FilterColumnNodeModel() {
        super(1, 1);
        m_list = new ArrayList<String>();
    }

    /**
     * Resets the internal list of columns to exclude.
     */
    protected void reset() {

    }

    /**
     * Creates a new <code>ColumnFilterTable</code> and returns it.
     * 
     * @param data The table for which to create the filtered output table
     * @param exec The execution monitor.
     * @return The filtered table.
     * @throws Exception if current settings are invalid
     * 
     * @see NodeModel#execute(BufferedDataTable[],ExecutionContext)
     */
    protected BufferedDataTable[] execute(final BufferedDataTable[] data,
            final ExecutionContext exec) throws Exception {

        assert (data != null && data.length == 1 && data[INPORT] != null);
        ColumnRearranger c = createColumnRearranger(data[0].getDataTableSpec());
        BufferedDataTable outTable = c.createTable(data[0], exec);
        return new BufferedDataTable[]{outTable};
    }

    /**
     * @see de.unikn.knime.core.node.NodeModel# saveInternals(java.io.File,
     *      de.unikn.knime.core.node.ExecutionMonitor)
     */
    protected void saveInternals(final File nodeInternDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {

        // nothing to be done
    }

    /**
     * @see de.unikn.knime.core.node.NodeModel# loadInternals(java.io.File,
     *      de.unikn.knime.core.node.ExecutionMonitor)
     */
    protected void loadInternals(final File nodeInternDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {

        // nothing to be done
    }

    /**
     * Excludes a number of columns from the input spec and generates a new
     * ouput spec.
     * 
     * @param inSpecs The input table spec.
     * @return outSpecs The output table spec with some excluded columns.
     * 
     * @throws InvalidSettingsException If the selected column is not available
     *             in the DataTableSpec.
     */
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
            throws InvalidSettingsException {
        assert (inSpecs != null);
        ColumnRearranger c = createColumnRearranger(inSpecs[INPORT]);
        return new DataTableSpec[]{c.createSpec()};

    }

    /*
     * Creates the output data table spec according to the current settings.
     * Throws an InvalidSettingsException if colums are specified that don't
     * exist in the input table spec.
     */
    private ColumnRearranger createColumnRearranger(final DataTableSpec inSpec)
            throws InvalidSettingsException {
        assert inSpec != null;
        // check if all specified columns exist in the input spec
        for (String name : m_list) {
            if (!inSpec.containsName(name)) {
                throw new InvalidSettingsException("Column '" + name
                        + "' not found.");
            }
        }
        // counter for included columns
        int j = 0;
        // compose list of included column indices
        // which are the original minus the excluded ones
        final int[] columns = new int[inSpec.getNumColumns() - m_list.size()];
        for (int i = 0; i < inSpec.getNumColumns(); i++) {
            // if exclude does not contain current column name
            if (!m_list.contains(inSpec.getColumnSpec(i).getName())) {
                columns[j] = i;
                j++;
            }
        }
        assert (j == columns.length);
        // return the new spec
        ColumnRearranger c = new ColumnRearranger(inSpec);
        c.keepOnly(columns);
        return c;
    }

    /**
     * Writes number of filtered columns, and the names as <code>DataCell</code>
     * to the given settings.
     * 
     * @param settings The object to save the settings into.
     */
    protected void saveSettingsTo(final NodeSettingsWO settings) {
        settings.addStringArray(KEY, m_list.toArray(new String[0]));
    }

    /**
     * Reads the filtered columns.
     * 
     * @param settings to read from.
     * @throws InvalidSettingsException If the settings does not contain the
     *             size or a particular column key.
     */
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        // clear exclude column list
        m_list.clear();
        // get list of excluded columns
        String[] columns = settings.getStringArray(KEY, m_list
                .toArray(new String[0]));
        for (int i = 0; i < columns.length; i++) {
            m_list.add(columns[i]);
        }
    }

    /**
     * @see NodeModel#validateSettings(NodeSettingsRO)
     */
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        // true because the filter model does not care if there are columns to
        // exclude are available
    }

} // FilterColumnNodeModel
