<Global.Microsoft.VisualBasic.CompilerServices.DesignerGenerated()> _
Partial Class IRRIGESTDB
    Inherits System.Windows.Forms.Form

    'Form reemplaza a Dispose para limpiar la lista de componentes.
    <System.Diagnostics.DebuggerNonUserCode()> _
    Protected Overrides Sub Dispose(ByVal disposing As Boolean)
        Try
            If disposing AndAlso components IsNot Nothing Then
                components.Dispose()
            End If
        Finally
            MyBase.Dispose(disposing)
        End Try
    End Sub

    'Requerido por el Diseñador de Windows Forms
    Private components As System.ComponentModel.IContainer

    'NOTA: el Diseñador de Windows Forms necesita el siguiente procedimiento
    'Se puede modificar usando el Diseñador de Windows Forms.  
    'No lo modifique con el editor de código.
    <System.Diagnostics.DebuggerStepThrough()> _
    Private Sub InitializeComponent()
        Me.components = New System.ComponentModel.Container
        Dim resources As System.ComponentModel.ComponentResourceManager = New System.ComponentModel.ComponentResourceManager(GetType(IRRIGESTDB))
        Me.BSTART = New System.Windows.Forms.Button
        Me.BRESET = New System.Windows.Forms.Button
        Me.BCONF = New System.Windows.Forms.Button
        Me.LSTATE = New System.Windows.Forms.Label
        Me.Timer1 = New System.Windows.Forms.Timer(Me.components)
        Me.LSTATE2 = New System.Windows.Forms.Label
        Me.Label1 = New System.Windows.Forms.Label
        Me.SuspendLayout()
        '
        'BSTART
        '
        resources.ApplyResources(Me.BSTART, "BSTART")
        Me.BSTART.Name = "BSTART"
        Me.BSTART.UseVisualStyleBackColor = True
        '
        'BRESET
        '
        resources.ApplyResources(Me.BRESET, "BRESET")
        Me.BRESET.Name = "BRESET"
        Me.BRESET.UseVisualStyleBackColor = True
        '
        'BCONF
        '
        resources.ApplyResources(Me.BCONF, "BCONF")
        Me.BCONF.Name = "BCONF"
        Me.BCONF.UseVisualStyleBackColor = True
        '
        'LSTATE
        '
        resources.ApplyResources(Me.LSTATE, "LSTATE")
        Me.LSTATE.Name = "LSTATE"
        '
        'Timer1
        '
        Me.Timer1.Interval = 1000
        '
        'LSTATE2
        '
        resources.ApplyResources(Me.LSTATE2, "LSTATE2")
        Me.LSTATE2.Name = "LSTATE2"
        '
        'Label1
        '
        resources.ApplyResources(Me.Label1, "Label1")
        Me.Label1.Name = "Label1"
        '
        'IRRIGESTDB
        '
        resources.ApplyResources(Me, "$this")
        Me.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font
        Me.Controls.Add(Me.Label1)
        Me.Controls.Add(Me.LSTATE2)
        Me.Controls.Add(Me.LSTATE)
        Me.Controls.Add(Me.BCONF)
        Me.Controls.Add(Me.BRESET)
        Me.Controls.Add(Me.BSTART)
        Me.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedSingle
        Me.Name = "IRRIGESTDB"
        Me.ResumeLayout(False)
        Me.PerformLayout()

    End Sub
    Friend WithEvents BSTART As System.Windows.Forms.Button
    Friend WithEvents BRESET As System.Windows.Forms.Button
    Friend WithEvents BCONF As System.Windows.Forms.Button
    Friend WithEvents LSTATE As System.Windows.Forms.Label
    Friend WithEvents Timer1 As System.Windows.Forms.Timer
    Friend WithEvents LSTATE2 As System.Windows.Forms.Label
    Friend WithEvents Label1 As System.Windows.Forms.Label

End Class
