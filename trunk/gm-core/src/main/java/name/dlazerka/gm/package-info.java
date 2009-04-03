/**
 * This package contains interfaces and classes for the GraphMagic core.
 * <p/>
 * Design notes:
 * <ul>
 * <li>
 * A new vertex cannot be obtained without calling {@link Graph#createVertex()}. This
 * also adds the newly created vertex to the graph's vertex set. After calling
 * {@link Graph#remove(Vertex)}, the {@link Vertex} instance becomes invalid and must
 * not be used any more.
 * <p/>
 * The same applied for edges.
 * </li>
 * <li>
 * Labeled graphs differes from unlabeled only on visual behaviour. Internally, all
 * vertices have their identifiers returned by {@link Vertex#getId()}. Basically
 * labels are painted using this id. If you want some other labeling you need to use
 * different visual painting mechanism.
 * </li>
 */
package name.dlazerka.gm;